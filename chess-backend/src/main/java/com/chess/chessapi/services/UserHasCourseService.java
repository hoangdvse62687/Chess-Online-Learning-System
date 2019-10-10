package com.chess.chessapi.services;

import com.chess.chessapi.constants.Status;
import com.chess.chessapi.entities.UserHasCourse;
import com.chess.chessapi.repositories.UserHasCourseRepository;
import com.chess.chessapi.utils.TimeUtils;
import com.chess.chessapi.viewmodels.UserDetailViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class UserHasCourseService {
    @Autowired
    private UserHasCourseRepository userHasCourseRepository;

    //public method
    public void create(long userId, long courseId, Timestamp enrolledDate,long statusId){
        this.userHasCourseRepository.create(userId,courseId,enrolledDate,statusId);
    }

    public void updateUserHasCourse(List<UserHasCourse> oldUserHasCourses, List<UserDetailViewModel> newUserHasCourses,long courseId){
        if(oldUserHasCourses == null || oldUserHasCourses.isEmpty()){
            //add all
            for (UserDetailViewModel newUserHasCourse:
                    newUserHasCourses) {
                this.create(newUserHasCourse.getUserId(),courseId
                        , TimeUtils.getCurrentTime(), Status.USER_HAS_COURSE_STATUS_IN_PROCESS);
            }
        }else if(newUserHasCourses != null && !newUserHasCourses.isEmpty()){
            for (UserDetailViewModel newUserHasCourse:
                    newUserHasCourses) {
                boolean isExist = false;
                for (UserHasCourse oldUserHasCourse:
                        oldUserHasCourses) {
                    if(newUserHasCourse.getUserId() == oldUserHasCourse.getUser().getUserId()){
                        isExist = true;
                        oldUserHasCourses.remove(oldUserHasCourse);
                        break;
                    }
                }
                if(!isExist){
                    this.create(newUserHasCourse.getUserId(),courseId
                            , TimeUtils.getCurrentTime(), Status.USER_HAS_COURSE_STATUS_IN_PROCESS);
                }
            }

            //delete old
            for (UserHasCourse oldUserHasCourse:
                    oldUserHasCourses) {
                this.userHasCourseRepository.delete(oldUserHasCourse);
            }
        }else{
            //delete all
            for (UserHasCourse oldUserHasCourse:
                oldUserHasCourses) {
                this.userHasCourseRepository.delete(oldUserHasCourse);
            }
        }
    }

    public List<UserHasCourse> getAllByCourseIdAndStatusId(long courseId,long statusId){
        return this.userHasCourseRepository.findAllByCourseIdAndStatusId(courseId,statusId);
    }

    public List<Long> getAllLearnerByCourseId(long courseId,long learnerId){
        return this.userHasCourseRepository.getAllLearnerByCourseId(courseId,learnerId);
    }

    public boolean isEnrolled(long courseId,long userId){
        if(this.userHasCourseRepository.isEnrolled(courseId,userId) == 1){
            return true;
        }else{
            return false;
        }
    }
    //end public method
}
