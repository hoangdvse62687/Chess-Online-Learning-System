package com.chess.chessapi.services;

import com.chess.chessapi.constants.*;
import com.chess.chessapi.entities.*;
import com.chess.chessapi.models.*;
import com.chess.chessapi.repositories.CourseRepository;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.utils.MailContentBuilderUtils;
import com.chess.chessapi.utils.ManualCastUtils;
import com.chess.chessapi.utils.TimeUtils;
import com.chess.chessapi.viewmodels.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserService userService;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserHasCourseService userHasCourseService;

    @Autowired
    private CategoryHasCourseService categoryHasCourseService;

    @Autowired
    private MailService mailService;

    @Autowired
    private MailContentBuilderUtils mailContentBuilderUtils;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCourseSuggestionService redisCourseSuggestionService;

    @Autowired
    private RedisCommonCourseItemFilterService redisCommonCourseItemFilterService;

    @Autowired
    private SuggestionAlgorithmService suggestionAlgorithmService;

    //Public method
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public Course create(CourseCreateViewModel courseCreateViewModel, long userId){
        //default setting when created course is inactive
        Course course = ManualCastUtils.castCourseCreateViewModelToCourse(courseCreateViewModel);
        course.setCreatedDate(TimeUtils.getCurrentTime());
        //manual mapping author
        User user = new User();
        user.setUserId(userId);
        course.setUser(user);
        Course savedCourse = courseRepository.save(course);
        for (Long categoryId:
                courseCreateViewModel.getListCategoryIds()) {
            this.categoryHasCourseService.create(categoryId,savedCourse.getCourseId());
        }
        //create mapping
        this.userHasCourseService.create(userId,savedCourse.getCourseId()
                , TimeUtils.getCurrentTime(), Status.USER_HAS_COURSE_STATUS_IN_PROCESS);
        return savedCourse;
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void publishCourse(CoursePublishViewModel coursePublishViewModel,Course course){
        this.notificationService.sendNotificationToAdmin(AppMessage.CREATE_NEW_COURSE,course.getName(),
                course.getImage(),ObjectType.COURSE,course.getCourseId());

        this.updateStatus(coursePublishViewModel.getCourseId(),Status.COURSE_STATUS_WAITING);
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void enrollCourse(long userId,Course course){
        this.userHasCourseService.create(userId,course.getCourseId()
                , TimeUtils.getCurrentTime(),Status.USER_HAS_COURSE_STATUS_IN_PROCESS);
    }

    public PagedList<CoursePaginationViewModel> getCoursePaginationByStatusId(String courseName
            ,int pageIndex, int pageSize, String statusId,long userId,String sortBy,String sortDirection)
            throws NumberFormatException{
        StoredProcedureQuery storedProcedureQuery = this.em.createNamedStoredProcedureQuery("getCoursePaginations");
        Common.storedProcedureQueryPaginationSetup(storedProcedureQuery,pageIndex,pageSize,sortBy,sortDirection);
        storedProcedureQuery.setParameter("courseName",courseName);
        storedProcedureQuery.setParameter("statusId",statusId);
        storedProcedureQuery.setParameter("userId",userId);

        storedProcedureQuery.execute();

        List<Object[]> rawData = storedProcedureQuery.getResultList();
        final long totalElements = Long.parseLong(storedProcedureQuery.getOutputParameterValue("totalElements").toString());
        return this.fillDataToPaginationCustom(rawData,totalElements,pageSize);
    }

    public PagedList<CoursePaginationViewModel> getCoursePaginationsByCategoryId(String courseName, String statusId
            ,int pageIndex,int pageSize,long categoryId,long userId,String sortBy,String sortDirection){
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("getCourseByCategoryId");
        Common.storedProcedureQueryPaginationSetup(query,pageIndex,pageSize,sortBy,sortDirection);
        query.setParameter("courseName",courseName);
        query.setParameter("statusId",statusId);
        query.setParameter("userId",userId);
        query.setParameter("categoryId",categoryId);

        query.execute();
        List<Object[]> rawData = query.getResultList();
        final long totalElements = Long.parseLong(query.getOutputParameterValue("totalElements").toString());
        return this.fillDataToPaginationCustom(rawData,totalElements,pageSize);
    }

    public PagedList<CoursePaginationViewModel> getCoursePaginationsByEloId(String courseName, String statusId
            ,int pageIndex,int pageSize,int eloId,long userId,String sortBy,String sortDirection){
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("getCoursesByEloId");
        Common.storedProcedureQueryPaginationSetup(query,pageIndex,pageSize,sortBy,sortDirection);
        query.setParameter("courseName",courseName);
        query.setParameter("statusId",statusId);
        query.setParameter("userId",userId);
        query.setParameter("eloId",eloId);

        query.execute();
        List<Object[]> rawData = query.getResultList();
        final long totalElements = Long.parseLong(query.getOutputParameterValue("totalElements").toString());
        return this.fillDataToPaginationCustom(rawData,totalElements,pageSize);
    }

    public void updateStatus(long courseId,long statusId){
        this.courseRepository.updateStatus(courseId,statusId,TimeUtils.getCurrentTime());
    }

    public Optional<Course> getCourseById(long id){
        return this.courseRepository.findById(id);
    }

    public List<CourseDetailViewModel> getCourseDetailsByUserId(long userId){
        //getting courses by userId
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("getCourseByUserId");
        query.setParameter("userId",userId);

        query.execute();
        //end getting courses by userid

        return ManualCastUtils.castListObjectToCourseDetails(query.getResultList());
    }

    public List<CourseDetailViewModel> getCourseDetailsByLessonId(long lessonId){
        //getting courses by userId
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("getCoursesByLessonId");
        query.setParameter("lessonId",lessonId);

        query.execute();
        //end getting courses by userid

        return ManualCastUtils.castListObjectToCourseDetails(query.getResultList());
    }

    public boolean checkPermissionReviewCourse(long courseId,UserPrincipal userPrincipal){
        if(userPrincipal == null){
            return false;
        }

        return this.callStoreProcedureCheckEnroll(courseId,userPrincipal.getId());
    }

    public boolean checkPermissionViewCourseDetail(long courseId,UserPrincipal userPrincipal){
        if(userPrincipal == null){
            return false;
        }

        //allow admin permission view course
        if(Long.parseLong(userPrincipal.getRole()) == AppRole.ROLE_ADMIN){
            return true;
        }

        return this.callStoreProcedureCheckEnroll(courseId,userPrincipal.getId());
    }

    public boolean checkPermissionModifyCourse(long courseId){
        //check current user has this course
        //only method authorized used this method => not check null
        UserPrincipal userPrincipal = this.userService.getCurrentUser();

        //it's only used instructor authentication => check for modifying course
        return this.callStoreProcedureCheckEnroll(courseId,userPrincipal.getId());
    }
    public boolean checkPermissionUpdateStatusCourse(long courseId){
        UserPrincipal userPrincipal = this.userService.getCurrentUser();
        long authorId = this.getAuthorIdByCourseId(courseId);
        if(authorId == userPrincipal.getId()){
            return true;
        }
        return false;
    }
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateCourse(Course course){
         this.courseRepository.updateCourse(course.getCourseId(),course.getName(),course.getDescription(),
                 course.getStatusId(),course.getImage(),course.getRequiredElo(),TimeUtils.getCurrentTime());
         //only get old has status in-process
        List<UserHasCourse> oldUserHasCourses = this.userHasCourseService
                .getAllByCourseIdAndStatusId(course.getCourseId(),Status.USER_HAS_COURSE_STATUS_IN_PROCESS);
        //update user has course
        List<UserDetailViewModel> totalUsers = new ArrayList<>();
        totalUsers.addAll(course.getTutors());
        totalUsers.addAll(course.getUserEnrolleds());
        this.userHasCourseService.updateUserHasCourse(oldUserHasCourses,totalUsers,course.getCourseId());
        //update category list id
        List<CategoryHasCourse> oldCategoryHasCourses = this.categoryHasCourseService
                .getAllByCourseId(course.getCourseId());
        this.categoryHasCourseService.UpdateCategoryHasCourse(oldCategoryHasCourses,course.getListCategorys(),course.getCourseId());
    }

    public boolean isExist(long courseId){
        return this.courseRepository.existsById(courseId);
    }
    public long getAuthorIdByCourseId(long courseId){
        return this.courseRepository.findAuthorIdByCourseId(courseId);
    }

    public PagedList<CoursePaginationViewModel> getCoursePaginationsByUserId(String courseName,int pageIndex,
              int pageSize,long userId,String sortBy,String sortDirection,String statusId){
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("getCoursePaginationsByUserid");
        query.setParameter("courseName",courseName);
        query.setParameter("statusId",statusId);
        query.setParameter("userId",userId);
        Common.storedProcedureQueryPaginationSetup(query,pageIndex,pageSize,sortBy,sortDirection);


        query.execute();

        List<Object[]> rawData = query.getResultList();
        final long totalElements = Long.parseLong(query.getOutputParameterValue("totalElements").toString());
        return this.fillDataToPaginationCustom(rawData,totalElements,pageSize);
    }

    public void updateCourseStatusByAdmin(Course course, CourseUpdateStatusViewModel courseUpdateStatusViewModel,User user){
        String messageNotification = "";

        //handle status admin can change
        String mailContent = course.getName();
        if(courseUpdateStatusViewModel.getStatusId() == Status.COURSE_STATUS_PUBLISHED){
            messageNotification = AppMessage.UPDATE_COURSE_STATUS_PUBLISHED;
            mailContent += AppMessage.PUBLISH_COURSE_REQUEST_CONTENT_PUBLISH;

            try{
                suggestionAlgorithmService.executeCommonItemFilterSuggestionAlgorithm(courseUpdateStatusViewModel.getCourseId());
            }catch (Exception ex){
                Logger.getLogger(CourseService.class.getName()).log(Level.SEVERE,null,ex);
            }

        }else{
            messageNotification = AppMessage.UPDATE_COURSE_STATUS_REJECTED;
            courseUpdateStatusViewModel.setStatusId(Status.COURSE_STATUS_REJECTED);
            mailContent += AppMessage.PUBLISH_COURSE_REQUEST_CONTENT_REJECT + courseUpdateStatusViewModel.getReasonReject();
        }

        //Send notification to author
        this.notificationService.sendNotificationToUser(messageNotification,course.getName(),course.getImage(),ObjectType.COURSE,
                course.getCourseId(),course.getUser().getUserId(),AppRole.ROLE_INSTRUCTOR);

        this.updateStatus(courseUpdateStatusViewModel.getCourseId(),courseUpdateStatusViewModel.getStatusId());
        //send email
        Mail mail = new Mail(AppMessage.PUBLISH_COURSE_REQUEST_SUBJECT,user.getEmail(),
                this.mailContentBuilderUtils.build(user.getFullName(),mailContent
                        , MailContentBuilderUtils.SOURCE_LINK_GO_TO_COURSE + course.getCourseId(),MailContentBuilderUtils.SOURCE_NAME_GO_TO_COURSE));

        this.mailService.sendMessage(mail);
    }

    public List<CourseForNotificationViewModel> getCourseForNotificationByListCourseId(List<Long> listCourseIds){
        if(listCourseIds == null){
            return null;
        }
        return ManualCastUtils.castListObjectsToCourseForNotificationViewModel
                (this.courseRepository.findCourseDetailForNotificationByListCourseId(listCourseIds));
    }

    public CourseForNotificationViewModel getCourseForNotificationByCourseId(Long listCourseIds){
        if(listCourseIds == 0){
            return null;
        }
        return ManualCastUtils.castObjectsToCourseForNotificationViewModel
                (this.courseRepository.findCourseDetailForNotificationByCourseId(listCourseIds));
    }

    public List<Long> getAllListCoursePulishedIds(){
        return this.courseRepository.findListCourseIdsByStatus(Status.COURSE_STATUS_PUBLISHED);
    }

    public PagedList<CoursePaginationViewModel> getCommonCourseSuggestion(int pageIndex,int pageSize,long courseId,long userId){
        CommonCourseItemSuggestionRedis data = this.redisCommonCourseItemFilterService.find(courseId);
        List<CourseUserFilterData> suggestions = new ArrayList<>();
        PagedList<CoursePaginationViewModel> paginations = null;
        if(data != null){
            suggestions = data.getCourseItemFilterData();
        }
        String listCourseIdArr = "";
        if(suggestions != null && !suggestions.isEmpty()){
            for (CourseUserFilterData item:
                    suggestions) {
                listCourseIdArr += item.getCourseId() + ",";
            }
            if(listCourseIdArr.length() > 0){
                listCourseIdArr.substring(0,listCourseIdArr.length()-1);
            }

            StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("getCommonCourseSuggestionPaginations");
            query.setParameter("listCourseIdStr",listCourseIdArr);
            query.setParameter("userId",userId);
            query.setParameter("statusId",Status.COURSE_STATUS_PUBLISHED);
            query.setParameter("totalElements",Long.parseLong("0"));
            query.setParameter("pageIndex",(pageIndex - 1) * pageSize);
            query.setParameter("pageSize",pageSize);

            query.execute();
            List<Object[]> rawData = query.getResultList();
            final long totalElements = Long.parseLong(query.getOutputParameterValue("totalElements").toString());
            paginations = this.fillDataToPaginationCustom(rawData,totalElements,pageSize);
            this.sortCourseSuggestionByArray(paginations,suggestions);
        }

        return paginations;
    }

    public PagedList<CoursePaginationViewModel> getCourseSuggestion(int pageIndex,int pageSize,long userId){
        int userEloId = EloRatingLevel.getIdByEloRange(this.userService.getPointByUserId(userId));
        CourseSuggestionRedis data = this.redisCourseSuggestionService.find(userId);
        List<CourseUserFilterData> suggestions = new ArrayList<>();
        if(data != null){
            if(data.isUsedCourseItemFilterData()){
                suggestions = data.getCourseItemFilterData();
            }else{
                suggestions = data.getCourseUserFilterData();
            }
        }
        int isListNull = 1;
        String listCourseIdArr = "";
        if(suggestions != null && !suggestions.isEmpty()){
            isListNull = 0;
            for (CourseUserFilterData item:
                    suggestions) {
                listCourseIdArr += item.getCourseId() + ",";
            }
            if(listCourseIdArr.length() > 0){
                listCourseIdArr.substring(0,listCourseIdArr.length()-1);
            }
        }

        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("getCourseSuggestionPaginations");
        query.setParameter("listCourseIdStr",listCourseIdArr);
        query.setParameter("userId",userId);
        //if learner out of beginner will suggestion beginner level
        query.setParameter("userEloId",userEloId == 0 ? 1 :  userEloId);
        query.setParameter("isListNull",isListNull);
        query.setParameter("totalElements",Long.parseLong("0"));
        query.setParameter("statusId",Status.COURSE_STATUS_PUBLISHED);
        query.setParameter("pageIndex",(pageIndex - 1) * pageSize);
        query.setParameter("pageSize",pageSize);

        query.execute();
        List<Object[]> rawData = query.getResultList();
        final long totalElements = Long.parseLong(query.getOutputParameterValue("totalElements").toString());
        PagedList<CoursePaginationViewModel> paginations = this.fillDataToPaginationCustom(rawData,totalElements,pageSize);
        if(isListNull != 1){
            this.sortCourseSuggestionByArray(paginations,suggestions);
        }

        return paginations;
    }

    public List<CoursePaginationViewModel> getListCourseSuggestionByEloIdAndStatusId(long statusId,long userId){
        List<Object[]> rawData = this.courseRepository.findListCourseSuggestionByStatusId(statusId,userId);
        return ManualCastUtils.castListObjectToCourseFromGetCoursePaginations(rawData,this.categoryService.getAllCategory());
    }

    public Course getLastCourseEnrollByUserId(long userId){
        return this.courseRepository.findLastCourseEnrollByUserId(userId);
    }
    //End Pulbic method

    //Private method
    private PagedList<CoursePaginationViewModel> fillDataToPaginationCustom(List<Object[]> rawData,long totalElements,int pageSize){
        long totalPages = (long) Math.ceil(totalElements / (double) pageSize);
        List<CoursePaginationViewModel> data = ManualCastUtils.castListObjectToCourseFromGetCoursePaginations(rawData,this.categoryService.getAllCategory());
        return new PagedList<CoursePaginationViewModel>(Math.toIntExact(totalPages),totalElements,data);
    }

    private boolean callStoreProcedureCheckEnroll(long courseId,long userId){
        StoredProcedureQuery storedProcedureQuery = this.em.createNamedStoredProcedureQuery("checkEnrollUserCourse");
        storedProcedureQuery.setParameter("userId",userId);
        storedProcedureQuery.setParameter("courseId",courseId);
        storedProcedureQuery.setParameter("isEnrolled",true);

        storedProcedureQuery.execute();

        return Boolean.parseBoolean(storedProcedureQuery.getOutputParameterValue("isEnrolled").toString());
    }
    private void sortCourseSuggestionByArray(PagedList<CoursePaginationViewModel> data,List<CourseUserFilterData> suggestions){
        List<CoursePaginationViewModel> sortedData = new ArrayList<>();
        suggestions.forEach((suggestion) -> {
            data.getContent().forEach((item) -> {
                if(item.getCourseId() == suggestion.getCourseId()){
                    sortedData.add(item);
                }
            });
        });
        data.setContent(sortedData);
    }
    //Public method
}
