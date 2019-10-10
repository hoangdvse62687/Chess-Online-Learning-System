package com.chess.chessapi.services;

import com.chess.chessapi.constants.Status;
import com.chess.chessapi.entities.*;
import com.chess.chessapi.repositories.*;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.utils.ManualCastUtils;
import com.chess.chessapi.utils.TimeUtils;
import com.chess.chessapi.viewmodels.LearningLogCreateResponse;
import com.chess.chessapi.viewmodels.LearningLogCreateViewModel;
import com.chess.chessapi.viewmodels.LearningLogViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LearningLogService {
    @Autowired
    private LearningLogRepository learningLogRepository;

    @Autowired
    private UserHasCourseRepository userHasCourseRepository;

    @Autowired
    private CourseHasLessonRepository courseHasLessonRepository;

    //PUBLIC DEFINED
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public LearningLogCreateResponse create(LearningLogCreateViewModel learningLogCreateViewModel,long userId){
        LearningLog learningLog = new LearningLog();
        Course course = new Course();
        course.setCourseId(learningLogCreateViewModel.getCourseId());
        learningLog.setCourse(course);
        learningLog.setFinishedDate(TimeUtils.getCurrentTime());
        Lesson lesson = new Lesson();
        lesson.setLessonId(learningLogCreateViewModel.getLessonId());
        learningLog.setLesson(lesson);
        learningLog.setPassed(learningLogCreateViewModel.isPassed());
        User user = new User();
        user.setUserId(userId);
        learningLog.setUser(user);

        LearningLog savedLearningLog = this.learningLogRepository.save(learningLog);

        LearningLogCreateResponse learningLogCreateResponse = new LearningLogCreateResponse();
        learningLogCreateResponse.setSavedId(savedLearningLog.getLearninglogId());
        LearningLogViewModel learningLogViewModel = new LearningLogViewModel();
        learningLogViewModel.setPassed(learningLogCreateViewModel.isPassed());
        learningLogViewModel.setLearningLogId(learningLogCreateViewModel.getLessonId());
        learningLogCreateResponse.setComplete(this.isCompleteLearning(learningLogCreateViewModel.getCourseId()
                ,userId,learningLogViewModel));
        return learningLogCreateResponse;
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public boolean update(LearningLog learningLog,boolean isPassed){
        learningLog.setPassed(isPassed);
        this.learningLogRepository.save(learningLog);

        LearningLogViewModel learningLogViewModel = new LearningLogViewModel();
        learningLogViewModel.setPassed(isPassed);
        learningLogViewModel.setLearningLogId(learningLog.getLesson().getLessonId());
        return this.isCompleteLearning(learningLog.getCourse().getCourseId()
                ,learningLog.getUser().getUserId(),learningLogViewModel);
    }

    public List<LearningLogViewModel> getAllByCourseId(long courseId, long userId){
        return ManualCastUtils.castListObjectsToLearningLogViewModel(this.learningLogRepository.findAllByCourseIdAndUserId(courseId,userId));
    }

    public void deleteAllByLessonId(long lessonId){
        this.learningLogRepository.deleteAllByLessonId(lessonId);
    }

    public void deleteAllByLessonIdAndCourseId(long lessonId,long courseId){
        this.learningLogRepository.deleteAllByLessonIdAndCourseId(lessonId,courseId);
    }

    public Optional<LearningLog> getById(long learningLogId){
        return this.learningLogRepository.findById(learningLogId);
    }

    public boolean isExist(long userId,long courseId,long lessonId){
        return this.learningLogRepository.isExist(userId,courseId,lessonId);
    }
    //END PUBLIC DEFINED

    //PRIVATE DEFINED
    private boolean isCompleteLearning(long courseId,long userId,LearningLogViewModel learningLogCreateOrUpdate){
        boolean result = false;

        UserHasCourse userHasCourse = this.userHasCourseRepository.findByCourseIdAndUserId(userId,courseId);
        List<Long> courseHasLessonIds = this.courseHasLessonRepository.findByCourseId(courseId);
        List<Long> learningLogsPassed = new ArrayList<>();
        List<LearningLogViewModel> learningLogs = this.getAllByCourseId(courseId,userId);
        if(courseHasLessonIds != null && learningLogs != null){
            if(!learningLogs.stream().filter(o -> o.getLearningLogId() == learningLogCreateOrUpdate.getLearningLogId())
                    .findFirst().isPresent()){
                learningLogs.add(learningLogCreateOrUpdate);
            }else{
                learningLogs.stream().filter(o -> o.getLearningLogId() == learningLogCreateOrUpdate.getLearningLogId())
                        .findFirst().get().setPassed(learningLogCreateOrUpdate.isPassed());
            }
            for (LearningLogViewModel item:
                 learningLogs) {
                if(item.isPassed()){
                    learningLogsPassed.add(item.getLearningLogId());
                }
            }
            int counterPass = 0;
            for (Long courseHasLessonId:
                    courseHasLessonIds) {
                if(learningLogsPassed.contains(courseHasLessonId)){
                    counterPass++;
                }
            }
            if(counterPass == courseHasLessonIds.size()){
                result = true;
            }

            if(userHasCourse.getStatusId() != Status.USER_HAS_COURSE_STATUS_PASSED  && userHasCourse.getStatusId() != Status.USER_HAS_COURSE_STATUS_NOT_PASSED
             && learningLogs.size() >= courseHasLessonIds.size() && !result){
                this.userHasCourseRepository.updateStatusByCourseIdAndUserId(courseId,userId,Status.USER_HAS_COURSE_STATUS_NOT_PASSED);
            }
        }

        if(userHasCourse.getStatusId() != Status.USER_HAS_COURSE_STATUS_PASSED && result){
            this.userHasCourseRepository.updateStatusByCourseIdAndUserId(courseId,userId,Status.USER_HAS_COURSE_STATUS_PASSED);
        }

        return result;
    }
    //END PRIVATE DEFINED
}
