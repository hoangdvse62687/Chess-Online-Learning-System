package com.chess.chessapi.services;

import com.chess.chessapi.constants.*;
import com.chess.chessapi.entities.Certificate;
import com.chess.chessapi.entities.User;
import com.chess.chessapi.models.Mail;
import com.chess.chessapi.models.PagedList;
import com.chess.chessapi.repositories.UserRepository;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.utils.MailContentBuilderUtils;
import com.chess.chessapi.utils.ManualCastUtils;
import com.chess.chessapi.utils.TimeUtils;
import com.chess.chessapi.viewmodels.UserDetailViewModel;
import com.chess.chessapi.viewmodels.UserPaginationViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CertificatesService certificatesService;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CourseService courseService;

    @Autowired
    private MailService mailService;

    @Autowired
    private MailContentBuilderUtils mailContentBuilderUtils;

    @Autowired
    private PointLogService pointLogService;

    @Autowired
    private RedisUserPrincipleService redisUserPrincipleService;

    private final String START_ELO_MESSAGE = "Bạn đang ở ELO ";
    private final String CONGRATULATION_ELO_MESSAGE = ". Chức bạn luyện tập vui vẻ";
    public UserPrincipal getCurrentUser(){
        UserPrincipal user = null;
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            user = (UserPrincipal) authentication.getPrincipal();
        }catch (NullPointerException ex){

        }catch (ClassCastException ex){

        }
        return user;
    }

    public Optional<User> getUserById(long id){
        return this.userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email){return userRepository.findByEmail(email);}

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public User register(User user){
        if(user.getRoleId() == AppRole.ROLE_INSTRUCTOR){
            this.registerInstructor(user);
        }else {
            this.registerLearner(user);
        }
        for (Certificate c:
             user.getCertificates()) {
            this.certificatesService.create(c.getCertificateLink(),user.getUserId());
        }

        this.pointLogService.create(START_ELO_MESSAGE + user.getPoint() + CONGRATULATION_ELO_MESSAGE
                ,user.getPoint(),user.getUserId());

        this.userRepository.updateRegister(user.getUserId(),user.getFullName(),user.getAchievement(),user.getPoint(),
                user.getRoleId(),user.isActive(),user.getAvatar(),user.isReviewed(), TimeUtils.getCurrentTime());

        this.setUserRoleAuthentication(user);
        this.updateUserDetailsOnRedis(user,false,false);
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateProfile(User user){
        this.userRepository.updateProfile(user.getUserId(),user.getFullName(),user.getAchievement()
                ,user.getAvatar(),TimeUtils.getCurrentTime());

        //handle cetificate update
        List<Certificate> oldCetificates = this.certificatesService.findAllByUserId(user.getUserId());

        this.certificatesService.updateCertifications(oldCetificates,user.getCertificates(),user.getUserId());

        this.updateUserDetailsOnRedis(user,false,false);
    }

    public PagedList<UserPaginationViewModel> getPagination(int page, int pageSize, String email, String role, String isActive,String isReviewed){
        PageRequest pageable =  null;
        pageable = PageRequest.of(page - 1,pageSize, Sort.by(EntitiesFieldName.USER_CREATED_DATE).descending());

        Page<Object> rawData = null;
        if(!role.isEmpty() && !isActive.isEmpty() && !isReviewed.isEmpty()){
            rawData = this.userRepository.findAllByFullNameFilterRoleAndStatusAndReviewed
                    (pageable,email,'%' + role + '%',Boolean.valueOf(isActive),Boolean.valueOf(isReviewed));
        }
        else if(!role.isEmpty() && !isActive.isEmpty()){
            rawData = this.userRepository.findAllByFullNameFilterRoleAndStatus(pageable,email,'%' + role + '%',Boolean.valueOf(isActive));
        }else if(!isActive.isEmpty()){
            rawData = this.userRepository.findAllByFullNameFilterStatus(pageable,email,Boolean.valueOf(isActive));
        }else if(!role.isEmpty()){
            rawData = this.userRepository.findAllByFullNameFilterRole(pageable,email,'%' + role + '%');
        }else{
            rawData = this.userRepository.findAllByFullNameCustom(pageable,email);
        }

        return this.fillDataToPaginationCustom(rawData);
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateStatus(User user,long userId,boolean isActive){
        if(!user.isReviewed() && !isActive){
            this.userRepository.deleteUser(user.getUserId());
            this.redisUserPrincipleService.deleteById(user.getUserId());
        }else{
            this.userRepository.updateStatus(userId,isActive,true);
            if(user.getRoleId() != AppRole.ROLE_ADMIN){
                //notification send to user
                this.notificationService.sendNotificationToUser(isActive ? AppMessage.UPDATE_USER_STATUS_ACTIVE : AppMessage.UPDATE_USER_STATUS_INACTIVE,
                        user.getEmail(),user.getAvatar(),ObjectType.USER,userId,userId,user.getRoleId());
                //send email
                Mail mail = new Mail(isActive ? AppMessage.ACCEPT_REQUEST_SUBJECT : AppMessage.REJECT_REQUEST_SUBJECT,user.getEmail(),
                        this.mailContentBuilderUtils.build(user.getFullName(),isActive ? AppMessage.ACCEPT_REQUEST_CONTENT : AppMessage.REJECT_REQUEST_CONTENT
                                ,MailContentBuilderUtils.SOURCE_LINK_GO_TO_PROFILE,MailContentBuilderUtils.SOURCE_NAME_GO_TO_PROFILE));
                this.mailService.sendMessage(mail);
            }

            user.setActive(isActive);
            this.updateUserDetailsOnRedis(user,true,isActive);
        }
    }

    public void getUserDetails(User user){
        if(user != null){
            user.setCourseDetailViewModels(this.courseService.getCourseDetailsByUserId(user.getUserId()));
        }
    }

    public boolean checkPermissionModify(long userId){
        UserPrincipal currentUser = this.getCurrentUser();
        if(userId == currentUser.getId()){
            return true;
        }
        return false;
    }

    public List<UserDetailViewModel> getUserDetailsByCourseId(long courseId){
        //getting users by courseid only get the in-process
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("getUsersByCourseid");
        query.setParameter("courseId",courseId);
        query.execute();

        //end getting users by courseid
        return ManualCastUtils.castListObjectToUserDetailsFromGetUsersByCourseid(query.getResultList());
    }

    public List<UserDetailViewModel> getUserEnrolls(List<UserDetailViewModel> userDetailViewModels){
        List<UserDetailViewModel> userEnrolls = new ArrayList<>();
        for (UserDetailViewModel user:
             userDetailViewModels) {
            if(user.getRoleId() == AppRole.ROLE_LEARNER){
                userEnrolls.add(user);
            }
        }
        return userEnrolls;
    }

    public List<UserDetailViewModel> getTutors(List<UserDetailViewModel> userDetailViewModels){
        List<UserDetailViewModel> tutors = new ArrayList<>();
        for (UserDetailViewModel user:
                userDetailViewModels) {
            if(user.getRoleId() == AppRole.ROLE_INSTRUCTOR){
                tutors.add(user);
            }
        }
        return tutors;
    }

    public void increasePoint(long userId,float point){
        this.userRepository.increasePoint(userId,(int)point);
    }

    public int getPointByUserId(long userId){
        return this.userRepository.findPointByUserId(userId);
    }
    public boolean isExist(long userId){
        return this.userRepository.existsById(userId);
    }

    List<Long> getAllListLearnerIds(){
        return this.userRepository.findAllListUserIdsByRole(AppRole.ROLE_LEARNER);
    }
    // private method
    private void setUserRoleAuthentication(User user){
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority(Long.toString(user.getRoleId())));
        UserPrincipal userDetails = UserPrincipal.create(user);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        authentication.setDetails(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void updateUserDetailsOnRedis(User user,boolean isUpdateStatus,boolean status){

        if(isUpdateStatus){
            user.setActive(status);
        }

        this.redisUserPrincipleService.update(UserPrincipal.create(user));
    }
    private void registerLearner(User user){
        user.setActive(Status.ACTIVE);
        user.setRoleId(AppRole.ROLE_LEARNER);
        user.setPoint(user.getPoint());
        user.setReviewed(true);
    }

    private void registerInstructor(User user){
        user.setActive(Status.INACTIVE);
        user.setRoleId(AppRole.ROLE_INSTRUCTOR);
        user.setPoint(0);
        user.setReviewed(false);
        // create notification for admin
        this.notificationService.sendNotificationToAdmin(AppMessage.CREATE_NEW_USER_AS_INSTRUCTOR,user.getEmail(),
                user.getAvatar(),ObjectType.USER,user.getUserId());
    }

    private PagedList<UserPaginationViewModel> fillDataToPaginationCustom(Page<Object> rawData){
        final List<UserPaginationViewModel> content = ManualCastUtils.castPageObjectsToUser(rawData);
        final int totalPages = rawData.getTotalPages();
        final long totalElements = rawData.getTotalElements();
        return new PagedList<UserPaginationViewModel>(totalPages,totalElements,content);
    }
    // end private method
}
