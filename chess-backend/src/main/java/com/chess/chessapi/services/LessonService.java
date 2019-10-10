package com.chess.chessapi.services;

import com.chess.chessapi.constants.AppRole;
import com.chess.chessapi.constants.Common;
import com.chess.chessapi.constants.ObjectType;
import com.chess.chessapi.entities.*;
import com.chess.chessapi.factories.LessonFactory;
import com.chess.chessapi.models.PagedList;
import com.chess.chessapi.repositories.LessonRepository;
import com.chess.chessapi.security.UserPrincipal;
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
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class LessonService {
    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseHasLessonService courseHasLessonService;

    @Autowired
    private UserService userService;

    @Autowired
    private LearningLogService learningLogService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserHasCourseService userHasCourseService;

    @PersistenceContext
    private EntityManager em;

    private LessonFactory lessonFactory = new LessonFactory();
    private final String CREATE_LESSON_NOTIFICATION_MESSAGE = " đã thêm bài học ";
    private final String UPDATE_LESSON_NOTIFICATION_MESSAGE = " đã cập nhật bài học ";
    private final String CREATE_EXECERCISE_NOTIFICATION_MESSAGE = " đã thêm bài tập mới ";
    private final String UPDATE_EXECERCISE_NOTIFICATION_MESSAGE = " đã cập nhật bài tập ";

    //PUBLIC METHOD DEFINED
    public Lesson getById(long id){
        Lesson lesson = this.lessonRepository.findById(id).get();
        if(lesson != null){
            lesson.setLessonContent(this.lessonFactory.getContent(lesson.getContent(),lesson.getLessonType()));
        }
        return lesson;
    }

    public List<LessonViewModel> getLessonByCourseId(long courseId){
        //getting courses by userId
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("getLessonByCourseId");
        query.setParameter("courseId",courseId);

        query.execute();
        //end getting courses by userid

        return ManualCastUtils.castListObjectToLessonViewModel(query.getResultList());
    }

    public long getAuthorLessonByLessonId(long lessonId){
        return this.lessonRepository.findLessonAuthorByLessonId(lessonId);
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public long createInteractiveLesson(InteractiveLessonCreateViewModel lessonViewModel, long userId){
        //Create Lesson
        Lesson savedLesson = this.createLesson(lessonViewModel.getName(),this.lessonFactory.toJson(lessonViewModel.getInteractiveLesson(),ObjectType.INTERACTIVE_LESSON)
                ,lessonViewModel.getDescription(),userId,ObjectType.INTERACTIVE_LESSON);

        //create mapping course has lesson in case has course id
        this.createLessonCourseMapping(lessonViewModel.getCourseId(),savedLesson.getLessonId()
                ,savedLesson.getName(),CREATE_LESSON_NOTIFICATION_MESSAGE);
        return savedLesson.getLessonId();
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public long createUninteractiveLesson(UninteractiveLessonCreateViewModel uninteractiveLessonCreateViewModel,long userId){
        //Create Lesson
        Lesson savedLesson = this.createLesson(uninteractiveLessonCreateViewModel.getName(),this.lessonFactory.toJson(uninteractiveLessonCreateViewModel.getUninteractiveLesson(),ObjectType.UNINTERACTIVE_LESSON)
                ,uninteractiveLessonCreateViewModel.getDescription(),userId, ObjectType.UNINTERACTIVE_LESSON);

        //create mapping course has lesson in case has course id
        this.createLessonCourseMapping(uninteractiveLessonCreateViewModel.getCourseId(),savedLesson.getLessonId()
                ,savedLesson.getName(),CREATE_LESSON_NOTIFICATION_MESSAGE);
        return savedLesson.getLessonId();
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public long createExerciseLesson(ExerciseLessonCreateViewModel exerciseLessonCreateViewModel, long userId){
        //Create Lesson
        Lesson savedLesson = this.createLesson(exerciseLessonCreateViewModel.getName(),this.lessonFactory.toJson(exerciseLessonCreateViewModel.getExercise(),ObjectType.EXERCISE)
                ,exerciseLessonCreateViewModel.getDescription(),userId,ObjectType.EXERCISE);

        //create mapping course has lesson in case has course id
        this.createLessonCourseMapping(exerciseLessonCreateViewModel.getCourseId(),savedLesson.getLessonId()
                ,savedLesson.getName(),CREATE_EXECERCISE_NOTIFICATION_MESSAGE);
        return savedLesson.getLessonId();
    }

    public void updateInteractiveLesson(InteractiveLessonUpdateViewModel lessonViewModel){
        //update lesson
        this.updateLesson(lessonViewModel.getLessonId(),lessonViewModel.getName(),this.lessonFactory.toJson(lessonViewModel.getInteractiveLesson(),ObjectType.INTERACTIVE_LESSON)
                ,lessonViewModel.getDescription(),UPDATE_LESSON_NOTIFICATION_MESSAGE);

    }

    public void updateExerciseLesson(ExerciseLessonUpdateViewModel lessonViewModel){
        //update lesson
        this.updateLesson(lessonViewModel.getLessonId(),lessonViewModel.getName(),this.lessonFactory.toJson(lessonViewModel.getExercise(),ObjectType.EXERCISE)
                ,lessonViewModel.getDescription(),UPDATE_EXECERCISE_NOTIFICATION_MESSAGE);
    }


    public void updateUninteractiveLesson(UninteractiveLessonUpdateViewModel uninteractiveLessonUpdateViewModel){
        //update lesson
        this.updateLesson(uninteractiveLessonUpdateViewModel.getLessonId(),uninteractiveLessonUpdateViewModel.getName(),this.lessonFactory.toJson(uninteractiveLessonUpdateViewModel.getUninteractiveLesson(),ObjectType.UNINTERACTIVE_LESSON)
                ,uninteractiveLessonUpdateViewModel.getDescription(),UPDATE_LESSON_NOTIFICATION_MESSAGE);

    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateLesson(long lessonId,String name,String content,String description,String contentNotification){
        this.lessonRepository.update(lessonId,name,content,description,TimeUtils.getCurrentTime());
        this.sendNotification(lessonId
                ,contentNotification + name,null);
    }

    public PagedList<LessonViewModel> getAllByOwner(int pageIndex, int pageSize, String name, long userId,String sortBy,String sortDirection){
        StoredProcedureQuery storedProcedureQuery = this.em.createNamedStoredProcedureQuery("getLessonPaginationByUserid");
        Common.storedProcedureQueryPaginationSetup(storedProcedureQuery,pageIndex,pageSize,sortBy,sortDirection);
        storedProcedureQuery.setParameter("userId",userId);
        storedProcedureQuery.setParameter("lessonName",name);


        storedProcedureQuery.execute();

        List<Object[]> rawData = storedProcedureQuery.getResultList();
        final long totalElements = Long.parseLong(storedProcedureQuery.getOutputParameterValue("totalElements").toString());
        return this.fillDataToPagination(rawData,totalElements,pageSize);
    }

    public boolean checkPermissionModifyLesson(long lessonId){
        UserPrincipal userPrincipal = this.userService.getCurrentUser();
        if(userPrincipal != null){
            long ownerLessonId = this.lessonRepository.findLessonAuthorByLessonId(lessonId);
            if(ownerLessonId == userPrincipal.getId()){
                return true;
            }
        }

        return false;
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void removeLesson(Lesson lesson){
        //remove all learning log
        this.learningLogService.deleteAllByLessonId(lesson.getLessonId());

        //delete mapping with course
        this.courseHasLessonService.deleteAllByLessonId(lesson.getLessonId());
        //delete lesson
        this.lessonRepository.delete(lesson);
    }

    public boolean checkPermissionViewLesson(UserPrincipal userPrincipal,long lessonId){
        if(userPrincipal == null){
            return false;
        }

        //allow admin permission view course
        if(Long.parseLong(userPrincipal.getRole()) == AppRole.ROLE_ADMIN){
            return true;
        }
        StoredProcedureQuery storedProcedureQuery = this.em.createNamedStoredProcedureQuery("checkPermssionToViewLesson");
        storedProcedureQuery.setParameter("userId",userPrincipal.getId());
        storedProcedureQuery.setParameter("lessonId",lessonId);
        storedProcedureQuery.setParameter("hasPermission",true);

        storedProcedureQuery.execute();

        return Boolean.parseBoolean(storedProcedureQuery.getOutputParameterValue("hasPermission").toString());
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void mappingLessonCourse(long lessonId,String lessonName,long courseId){
        int lessonOrder = this.courseHasLessonService.getLastestLessonOrder(courseId);
        this.courseHasLessonService.create(lessonId,courseId,lessonOrder);

        List<Long> listCourseIds = new ArrayList<>();
        listCourseIds.add(courseId);
        this.sendNotification(lessonId
                ,CREATE_LESSON_NOTIFICATION_MESSAGE + lessonName,listCourseIds);
    }

    public boolean isExist(long lessonId){
        return this.lessonRepository.existsById(lessonId);
    }
    //END PUBLIC METHOD DEFINED

    //PRIVATE METHOD DEFINED
    private PagedList<LessonViewModel> fillDataToPagination(List<Object[]> rawData,long totalElements,int pageSize){
        final List<LessonViewModel> data = ManualCastUtils.castPageObjectToLessonViewModel(rawData);
        long totalPages = (long) Math.ceil(totalElements / (double) pageSize);
        return new PagedList<LessonViewModel>(Math.toIntExact(totalPages),totalElements,data);
    }

    private Lesson createLesson(String name,String content,String description,long userId,int type){
        Lesson lesson = new Lesson();
        lesson.setName(name);
        lesson.setContent(content);
        lesson.setCreatedDate(TimeUtils.getCurrentTime());
        lesson.setLessonType(type);
        lesson.setDescription(description);
        User user = new User();
        user.setUserId(userId);
        lesson.setUser(user);
        return this.lessonRepository.save(lesson);
    }

    private void sendNotification(long lessonId,String content,List<Long> listCourseIds){
        //allow send continue even errors occurs
        List<CourseForNotificationViewModel> courseForNotificationViewModels = new ArrayList<>();
        if(listCourseIds != null){
            courseForNotificationViewModels = this.courseService
                    .getCourseForNotificationByListCourseId(listCourseIds);
        }else{
            courseForNotificationViewModels = this.courseService
                    .getCourseForNotificationByListCourseId(this.courseHasLessonService.getListCourseIdByLessonId(lessonId));
        }
        if(courseForNotificationViewModels != null){
            try{
                for(CourseForNotificationViewModel courseForNotificationViewModel:
                courseForNotificationViewModels){
                    List<Long> listUserIds = this.userHasCourseService.getAllLearnerByCourseId
                            (courseForNotificationViewModel.getCourseId(), AppRole.ROLE_LEARNER);
                    for(Long userId: listUserIds){
                        this.notificationService.sendNotificationToUser(content,courseForNotificationViewModel.getCourseName()
                                ,courseForNotificationViewModel.getCourseImage(),ObjectType.COURSE,courseForNotificationViewModel.getCourseId()
                                ,userId,AppRole.ROLE_LEARNER);
                    }
                    this.notificationService.sendNotificationToAdmin(content,courseForNotificationViewModel.getCourseName()
                            ,courseForNotificationViewModel.getCourseImage(),ObjectType.COURSE,courseForNotificationViewModel.getCourseId());
                }
            }catch (Exception ex){
                //will write in logger later
                Logger.getLogger(LessonService.class.getName()).log(Level.SEVERE,null,ex);
            }
        }
    }

    private void createLessonCourseMapping(long courseId,long lessonId,String lessonName,String content){
        if(courseId != 0){
            int lessonOrder = this.courseHasLessonService.getLastestLessonOrder(courseId);
            lessonOrder++;
            this.courseHasLessonService.create(lessonId
                    ,courseId,lessonOrder);

            this.sendNotification(lessonId
                    ,content + lessonName,null);
        }
    }
    //END PRIVATE METHOD DEFINED
}
