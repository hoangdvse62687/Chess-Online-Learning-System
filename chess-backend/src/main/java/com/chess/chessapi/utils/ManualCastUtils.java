package com.chess.chessapi.utils;

import com.chess.chessapi.constants.EloRatingLevel;
import com.chess.chessapi.constants.Status;
import com.chess.chessapi.entities.*;
import com.chess.chessapi.models.StepSuggest;
import com.chess.chessapi.viewmodels.*;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ManualCastUtils implements Serializable {
    //USER DEFINED
    private static final int USER_ID_INDEX = 0;
    private static final int USER_EMAIL_INDEX = 1;
    private static final int USER_ROLE_INDEX = 2;
    private static final int USER_ISACTIVE_INDEX = 3;
    private static final int USER_AVATAR_INDEX = 4;
    private static final int USER_FULLNAME_INDEX = 5;
    private static final int USER_CREATEDDATE_INDEX = 6;
    //END USER DEFINED

    //COURSE DEFINED
    private static final int COURSE_ID_INDEX = 0;
    private static final int COURSE_NAME_INDEX = 1;
    private static final int COURSE_STATUS_ID_INDEX = 2;
    private static final int COURSE_IMAGE_INDEX = 3;
    private static final int COURSE_DESCRIPTION_INDEX = 4;
    private static final int COURSE_CREATED_DATE_INDEX = 5;
    private static final int COURSE_REQUIRED_ELO_INDEX = 6;
    private static final int COURSE_USERID_INDEX = 7;
    private static final int COURSE_USER_FULLNAME_INDEX = 8;
    private static final int COURSE_USER_AVATAR_INDEX = 9;
    private static final int COURSE_USER_IS_ENROLLED = 10;
    private static final int COURSE_CATEGORY_IDS= 11;
    private static final int COURSE_RATING_INDEX= 12;
    private static final int COURSE_TOTAL_RATING_INDEX= 13;
    private static final int COURSE_LEARNING_PROCESS_PERCENT = 14;
    //END COURSE DEFINED

    //LESSON DEFINED
    private static final int LESSON_ID_INDEX = 0;
    private static final int LESSON_NAME_INDEX = 1;
    private static final int LESSON_DESCRIPTION_INDEX = 2;
    private static final int LESSON_CREATED_DATE_INDEX = 3;
    private static final int LESSON_TYPE_INDEX = 4;
    private static final int LESSON_ORDER_LESSON_INDEX = 5;
    //END LESSON DEFINED

    //NULL VALUE DEFINED
    private static final String BOOLEAN_DEFAULT = "false";
    private static final String STRING_DEFAULT = "";
    private static final String NUMBER_DEFAULT = "0";
    //END NULL VALUE DEFINED

    //COURSE VIEW MODEL DEFINED
    private static final int COURSE_VIEW_MODEL_ID_INDEX = 0;
    private static final int COURSE_VIEW_MODEL_NAME_INDEX = 1;
    private static final int COURSE_VIEW_MODEL_STATUS_ID_INDEX = 2;
    private static final int COURSE_VIEW_MODEL_IMAGE_INDEX = 3;
    private static final int COURSE_VIEW_MODEL_CREATED_DATE_INDEX = 4;
    private static final int COURSE_VIEW_MODEL_AUTHOR_NAME_INDEX = 5;
    //END COURSE VIEW MODEL DEFINED

    //REVIEW VIEW MODEL DEFINED
    private static final int REVIEW_VIEW_MODEL_ID_INDEX = 0;
    private static final int REVIEW_VIEW_MODEL_RATING_INDEX = 1;
    private static final int REVIEW_VIEW_MODEL_CONTENT_INDEX = 2;
    private static final int REVIEW_VIEW_MODEL_USER_ID_INDEX = 3;
    private static final int REVIEW_VIEW_MODEL_CREATED_DATE_INDEX = 4;
    private static final int REVIEW_VIEW_MODEL_MODIFIED_DATE_INDEX = 5;
    private static final int REVIEW_VIEW_MODEL_USER_FULLNAME_INDEX = 6;
    private static final int REVIEW_VIEW_MODEL_EMAIL_INDEX = 7;
    private static final int REVIEW_VIEW_MODEL_USER_AVATAR_INDEX = 8;
    //END REVIEW VIEW MODEL DEFINED

    //CATEGORY VIEW MODEL DEFINED
    private static final int CATEGORY_VIEW_MODEL_ID_INDEX = 0;
    private static final int CATEGORY_VIEW_MODEL_NAME_INDEX = 1;
    //END CATEGORY VIEW MODEL DEFINED

    //LEARNER STATUS PUBLISH COURSE REPORT
    private static final int LEARNER_STATUS_REPORT_COURSE_NAME_INDEX = 0;
    private static final int LEARNER_STATUS_REPORT_COURSE_STATUS_INDEX = 1;
    private static final int LEARNER_STATUS_REPORT_COURSE_IN_PROCESS = 2;
    private static final int LEARNER_STATUS_REPORT_COURSE_PASSED = 3;
    private static final int LEARNER_STATUS_REPORT_COURSE_NOT_PASSED = 4;
    //END LEARNER STATUS PUBLISH COURSE REPORT

    //NOTIFICATION DEFINED
    private static final int NOTIFICATION_ID_INDEX = 0;
    private static final int NOTIFICATION_OBJECT_ID_INDEX = 1;
    private static final int NOTIFICATION_OBJECT_NAME_INDEX = 2;
    private static final int NOTIFICATION_OBJECT_AVATAR_INDEX = 3;
    private static final int NOTIFICATION_OBJECT_TYPE_ID_INDEX = 4;
    private static final int NOTIFICATION_CONTENT_INDEX = 5;
    private static final int NOTIFICATION_IS_VIEWED_INDEX = 6;
    private static final int NOTIFICATION_CREATED_DATE_INDEX = 7;
    private static final int NOTIFICATION_ROLE_TARGET_INDEX = 8;
    private static final int NOTIFICATION_USER_ID_INDEX = 9;
    //END NOTIFICATION DEFINED

    //POINT LOG DEFINED
    public static final int POINT_LOG_CONTENT_INDEX = 0;
    public static final int POINT_LOG_POINT_INDEX = 1;
    public static final int POINT_LOG_CREATED_DATE_INDEX = 2;
    //END POINT LOG DEFINED

    //COURSE FOR NOTIFICATION DEFINED
    public static final int COURSE_FOR_NOTIFICATION_ID_INDEX = 0;
    public static final int COURSE_FOR_NOTIFICATION_NAME_INDEX = 1;
    public static final int COURSE_FOR_NOTIFICATION_IMAGE_INDEX = 2;
    //END COURSE FOR NOTIFICATION DEFINED

    //GAME HISTORY DEFINED
    public static final int GAMEHISTORY_VIEW_MODEL_ID_INDEX = 0;
    public static final int GAMEHISTORY_VIEW_MODEL_STARTTIME_INDEX = 1;
    public static final int GAMEHISTORY_VIEW_MODEL_LEVEL_INDEX = 2;
    public static final int GAMEHISTORY_VIEW_MODEL_GAMETIME_INDEX = 3;
    public static final int GAMEHISTORY_VIEW_MODEL_POINT_INDEX = 4;
    public static final int GAMEHISTORY_VIEW_MODEL_STATUS_INDEX = 5;
    //END GAME HISTORY DEFINED
    //LEARNING LOG DEFINED
    public static final int LEARNINGLOG_ID_INDEX = 0;
    public static final int LEARNINGLOG_IS_PASSED_INDEX = 1;
    //END LEARNING LOG DEFINED
    public static User castObjectToUserByFindCustom(Object object)
            throws NumberFormatException{
        if(object == null){
            return null;
        }
        User user = new User();
        Object[] data = (Object[]) object;
        user.setUserId(Long.parseLong(data[USER_ID_INDEX].toString()));
        user.setEmail(data[USER_EMAIL_INDEX].toString());
        user.setRoleId(Long.parseLong(data[USER_ROLE_INDEX].toString()));
        user.setActive(parseBoolean(data[USER_ISACTIVE_INDEX].toString()));
        return user;
    }

    public static CourseOverviewViewModel castObjectToCourseOverviewViewModel(List<Object> objects)
    throws NumberFormatException{
        if(objects == null){
            return null;
        }
        CourseOverviewViewModel courseOverviewViewModel = new CourseOverviewViewModel();
        Object[] data = (Object[]) objects.get(0);

        courseOverviewViewModel.setTotalQuantityRatings(Double.parseDouble(data[data.length - 2].toString()));

        for(int i = 0; i < data.length - 2;i++){
            OverviewRatingDetailsViewModel overviewRatingDetailsViewModel = new OverviewRatingDetailsViewModel();
            overviewRatingDetailsViewModel.setQuantity(Double.parseDouble(data[i].toString()));
            if(courseOverviewViewModel.getTotalQuantityRatings() != 0){
                overviewRatingDetailsViewModel.setRatio(overviewRatingDetailsViewModel.getQuantity()
                        / courseOverviewViewModel.getTotalQuantityRatings());
            }
            courseOverviewViewModel.getListRatings().add(overviewRatingDetailsViewModel);
        }
        courseOverviewViewModel.setTotalRatings(Float.parseFloat(handleNullValueObject(data[data.length - 1],Float.class)));
        return courseOverviewViewModel;
    }

    public static List<UserPaginationViewModel> castPageObjectsToUser(Page<Object> objects)
            throws NumberFormatException{
        List<UserPaginationViewModel> users = new ArrayList<>();
        for (Object object:
             objects.getContent()) {
            UserPaginationViewModel user = new UserPaginationViewModel();
            Object[] data = (Object[]) object;
            user.setUserId(Long.parseLong(data[USER_ID_INDEX].toString()));
            user.setEmail(data[USER_EMAIL_INDEX].toString());
            user.setRoleId(Integer.parseInt(data[USER_ROLE_INDEX].toString()));
            user.setIsActive(Integer.parseInt(data[USER_ISACTIVE_INDEX].toString()));
            user.setAvatar(data[USER_AVATAR_INDEX].toString());
            user.setFullName(data[USER_FULLNAME_INDEX].toString());
            user.setCreatedDate(Timestamp.valueOf(data[USER_CREATEDDATE_INDEX].toString()));
            users.add(user);
        }
        return users;
    }

    public static List<GameHistoryViewModel> castPageObjectsToGameHistoryViewModel(Page<Object> objects)
            throws NumberFormatException{
        List<GameHistoryViewModel> gameHistoryViewModels = new ArrayList<>();
        for (Object object:
                objects.getContent()) {
            GameHistoryViewModel gameHistoryViewModel = new GameHistoryViewModel();
            Object[] data = (Object[]) object;
            gameHistoryViewModel.setGamehistoryId(Long.parseLong(data[GAMEHISTORY_VIEW_MODEL_ID_INDEX].toString()));
            gameHistoryViewModel.setStartTime(Timestamp.valueOf(data[GAMEHISTORY_VIEW_MODEL_STARTTIME_INDEX].toString()));
            gameHistoryViewModel.setGameTime(Integer.parseInt(data[GAMEHISTORY_VIEW_MODEL_GAMETIME_INDEX].toString()));
            gameHistoryViewModel.setLevel(Integer.parseInt(data[GAMEHISTORY_VIEW_MODEL_LEVEL_INDEX].toString()));
            gameHistoryViewModel.setPoint(Float.parseFloat(data[GAMEHISTORY_VIEW_MODEL_POINT_INDEX].toString()));
            gameHistoryViewModel.setStatus(Integer.parseInt(data[GAMEHISTORY_VIEW_MODEL_STATUS_INDEX].toString()));
            gameHistoryViewModels.add(gameHistoryViewModel);
        }
        return gameHistoryViewModels;
    }

    public static List<CoursePaginationViewModel> castListObjectToCourseFromGetCoursePaginations(List<Object[]> objects,List<Category> categories)
    throws NumberFormatException{
        List<CoursePaginationViewModel> data = new ArrayList<>();
        for (Object[] object:
             objects) {
            CoursePaginationViewModel coursePaginationViewModel = new CoursePaginationViewModel();
            coursePaginationViewModel.setCourseId(Long.parseLong(object[COURSE_ID_INDEX].toString()));
            coursePaginationViewModel.setCourseName(object[COURSE_NAME_INDEX].toString());
            coursePaginationViewModel.setStatusId(Long.parseLong(object[COURSE_STATUS_ID_INDEX].toString()));
            coursePaginationViewModel.setCourseImage(handleNullValueObject(object[COURSE_IMAGE_INDEX],String.class));
            coursePaginationViewModel.setCourseDescription(handleNullValueObject(object[COURSE_DESCRIPTION_INDEX],String.class));
            coursePaginationViewModel.setCourseCreatedDate(Timestamp.valueOf(object[COURSE_CREATED_DATE_INDEX].toString()));
            coursePaginationViewModel.setRequiredElo(Integer.parseInt(object[COURSE_REQUIRED_ELO_INDEX].toString()));

            UserDetailViewModel author = new UserDetailViewModel();
            author.setUserId(Long.parseLong(object[COURSE_USERID_INDEX].toString()));
            author.setFullName(object[COURSE_USER_FULLNAME_INDEX].toString());
            author.setAvatar(handleNullValueObject(object[COURSE_USER_AVATAR_INDEX],String.class));

            coursePaginationViewModel.setAuthor(author);
            coursePaginationViewModel.setEnrolled(parseBoolean(object[COURSE_USER_IS_ENROLLED].toString()));
            List<CategoryViewModel> categoryViewModels = new ArrayList<>();
            String[] categoryIds = handleNullValueObject(object[COURSE_CATEGORY_IDS],String.class).split(",");
            for (String categoryId:
                 categoryIds) {
                if(!categoryId.isEmpty()){
                    for (Category category:
                         categories) {
                        if(category.getCategoryId() == Long.parseLong(categoryId)){
                            CategoryViewModel categoryViewModel = new CategoryViewModel();
                            categoryViewModel.setCategoryId(category.getCategoryId());
                            categoryViewModel.setName(category.getName());
                            categoryViewModels.add(categoryViewModel);
                        }
                    }
                }
            }
            coursePaginationViewModel.setListCategorys(categoryViewModels);
            coursePaginationViewModel.setRating(Double.parseDouble(object[COURSE_RATING_INDEX].toString()));
            coursePaginationViewModel.setTotalRating(Long.parseLong(object[COURSE_TOTAL_RATING_INDEX].toString()));
            double learningProcessPercent = Double.parseDouble(object[COURSE_LEARNING_PROCESS_PERCENT].toString());
            if(learningProcessPercent > 1){
                coursePaginationViewModel.setLearningProcessPercent(100);
            }else{
                coursePaginationViewModel.setLearningProcessPercent((int)(learningProcessPercent * 100));
            }
            data.add(coursePaginationViewModel);
        }
        return data;
    }

    public static List<CourseDetailViewModel> castListObjectToCourseDetailsFromGetCourseByCategoryId(List<Object[]> objects)
            throws NumberFormatException{
        List<CourseDetailViewModel> data = new ArrayList<>();
        for (Object[] object:
                objects) {
            CourseDetailViewModel courseDetailViewModel = new CourseDetailViewModel();
            courseDetailViewModel.setCourseId(Long.parseLong(object[COURSE_VIEW_MODEL_ID_INDEX].toString()));
            courseDetailViewModel.setName(object[COURSE_VIEW_MODEL_NAME_INDEX].toString());
            courseDetailViewModel.setStatusId(Long.parseLong(object[COURSE_VIEW_MODEL_STATUS_ID_INDEX].toString()));
            courseDetailViewModel.setImage(handleNullValueObject(object[COURSE_VIEW_MODEL_IMAGE_INDEX],String.class));
            courseDetailViewModel.setCreatedDate(Timestamp.valueOf(object[COURSE_VIEW_MODEL_CREATED_DATE_INDEX].toString()));
            courseDetailViewModel.setAuthorName(object[COURSE_VIEW_MODEL_AUTHOR_NAME_INDEX].toString());
            data.add(courseDetailViewModel);
        }
        return data;
    }

    public static List<CourseDetailViewModel> castListObjectToCourseDetails(List<Object[]> objects)
    throws NumberFormatException{
        List<CourseDetailViewModel> data = new ArrayList<>();
        for (Object[] object:
             objects) {
            CourseDetailViewModel courseDetailViewModel = new CourseDetailViewModel();
            courseDetailViewModel.setCourseId(Long.parseLong(object[COURSE_VIEW_MODEL_ID_INDEX].toString()));
            courseDetailViewModel.setName(object[COURSE_VIEW_MODEL_NAME_INDEX].toString());
            courseDetailViewModel.setStatusId(Long.parseLong(object[COURSE_VIEW_MODEL_STATUS_ID_INDEX].toString()));
            courseDetailViewModel.setImage(handleNullValueObject(object[COURSE_VIEW_MODEL_IMAGE_INDEX],String.class));
            data.add(courseDetailViewModel);
        }
        return data;
    }

    public static List<UserDetailViewModel> castListObjectToUserDetailsFromGetUsersByCourseid(List<Object[]> objects){
        List<UserDetailViewModel> data = new ArrayList<>();
        for (Object[] object:
                objects) {
            UserDetailViewModel userDetailViewModel = new UserDetailViewModel();
            userDetailViewModel.setUserId(Long.parseLong(object[USER_ID_INDEX].toString()));
            userDetailViewModel.setEmail(object[USER_EMAIL_INDEX].toString());
            userDetailViewModel.setRoleId(Integer.parseInt(object[USER_ROLE_INDEX].toString()));
            data.add(userDetailViewModel);
        }
        return data;
    }

    public static List<PointLogViewModel> castListObjectToPointLogPagination(List<Object[]> objects){
        List<PointLogViewModel> data = new ArrayList<>();
        for (Object[] object:
                objects) {
            PointLogViewModel pointLogViewModel = new PointLogViewModel();
            pointLogViewModel.setContent(object[POINT_LOG_CONTENT_INDEX].toString());
            pointLogViewModel.setPoint(Float.parseFloat(object[POINT_LOG_POINT_INDEX].toString()));
            pointLogViewModel.setCreatedDate(Timestamp.valueOf(object[POINT_LOG_CREATED_DATE_INDEX].toString()));
            data.add(pointLogViewModel);
        }
        return data;
    }

    public static List<LearnerStatusPublishCourseReportViewModel> castListObjectToLearnerStatusPublishCourseReport(List<Object[]> objects){
        List<LearnerStatusPublishCourseReportViewModel> data = new ArrayList<>();
        for (Object[] object:
                objects) {
            LearnerStatusPublishCourseReportViewModel learnerStatusPublishCourseReportViewModel =
                    new LearnerStatusPublishCourseReportViewModel();
            learnerStatusPublishCourseReportViewModel.setCourseName(object[LEARNER_STATUS_REPORT_COURSE_NAME_INDEX].toString());
            learnerStatusPublishCourseReportViewModel.setCourseStatus(Integer.parseInt(object[LEARNER_STATUS_REPORT_COURSE_STATUS_INDEX].toString()));
            learnerStatusPublishCourseReportViewModel.setCounterInProcessStatus(Integer.parseInt(object[LEARNER_STATUS_REPORT_COURSE_IN_PROCESS].toString()));
            learnerStatusPublishCourseReportViewModel.setCounterPassedStatus(Integer.parseInt(object[LEARNER_STATUS_REPORT_COURSE_PASSED].toString()));
            learnerStatusPublishCourseReportViewModel.setCounterNotPassedStatus(Integer.parseInt(object[LEARNER_STATUS_REPORT_COURSE_NOT_PASSED].toString()));
            data.add(learnerStatusPublishCourseReportViewModel);
        }
        return data;
    }

    public static List<Integer> castListObjectToListInteger(List<Object[]> objects){
        List<Integer> data = new ArrayList<>();
        for (Object[] object:
                objects) {
            for (Object item:
                    object) {
                data.add(Integer.parseInt(item.toString()));
            }
        }
        return data;
    }

    public static List<CategoryViewModel> castListObjectToCategoryIdFromGetCategoryByCourseId(List<Object[]> objects)
    throws NumberFormatException{
        List<CategoryViewModel> data = new ArrayList<>();
        for (Object[] object:
             objects) {
            CategoryViewModel categoryViewModel = new CategoryViewModel();
            categoryViewModel.setCategoryId(Long.parseLong(object[CATEGORY_VIEW_MODEL_ID_INDEX].toString()));
            categoryViewModel.setName(object[CATEGORY_VIEW_MODEL_NAME_INDEX].toString());
            data.add(categoryViewModel);
        }
        return data;
    }

    public static List<LessonViewModel> castListObjectToLessonViewModel(List<Object[]> objects)
    throws NumberFormatException{
        List<LessonViewModel> data = new ArrayList<>();
        for (Object[] object:
                objects) {
            LessonViewModel lessonViewModel = new LessonViewModel();
            lessonViewModel.setLessonId(Long.parseLong(object[LESSON_ID_INDEX].toString()));
            lessonViewModel.setName(object[LESSON_NAME_INDEX].toString());
            lessonViewModel.setDescription(handleNullValueObject(object[LESSON_DESCRIPTION_INDEX],String.class));
            lessonViewModel.setCreatedDate(Timestamp.valueOf(object[LESSON_CREATED_DATE_INDEX].toString()));
            lessonViewModel.setLessonOrdered(Integer.parseInt(object[LESSON_ORDER_LESSON_INDEX].toString()));
            lessonViewModel.setLessonType(Integer.parseInt(object[LESSON_TYPE_INDEX].toString()));
            data.add(lessonViewModel);
        }
        return data;
    }

    public static List<LessonViewModel> castPageObjectToLessonViewModel(List<Object[]>  objects)
            throws NumberFormatException{
        List<LessonViewModel> result = new ArrayList<>();
        for (Object object:
                objects) {
            LessonViewModel lessonViewModel = new LessonViewModel();
            Object[] data = (Object[]) object;
            lessonViewModel.setLessonId(Long.parseLong(data[LESSON_ID_INDEX].toString()));
            lessonViewModel.setName(data[LESSON_NAME_INDEX].toString());
            lessonViewModel.setDescription(handleNullValueObject(data[LESSON_DESCRIPTION_INDEX],String.class));
            lessonViewModel.setCreatedDate(Timestamp.valueOf(data[LESSON_CREATED_DATE_INDEX].toString()));
            lessonViewModel.setLessonType(Integer.parseInt(data[LESSON_TYPE_INDEX].toString()));
            result.add(lessonViewModel);
        }
        return result;
    }

    public static List<Notification> castListObjectToNotification(List<Object> objects){
        List<Notification> result = new ArrayList<>();
        for (Object object:
                objects) {
            Notification notification = new Notification();
            Object[] data = (Object[]) object;
            notification.setNotificationId(Long.parseLong(data[NOTIFICATION_ID_INDEX].toString()));
            notification.setObjectId(Long.parseLong(data[NOTIFICATION_OBJECT_ID_INDEX].toString()));
            notification.setObjectName(data[NOTIFICATION_OBJECT_NAME_INDEX].toString());
            notification.setObjectAvatar(data[NOTIFICATION_OBJECT_AVATAR_INDEX].toString());
            notification.setObjectTypeId(Long.parseLong(data[NOTIFICATION_OBJECT_TYPE_ID_INDEX].toString()));
            notification.setContent(data[NOTIFICATION_CONTENT_INDEX].toString());
            notification.setViewed(parseBoolean(data[NOTIFICATION_IS_VIEWED_INDEX].toString()));
            notification.setCreateDate(Timestamp.valueOf(data[NOTIFICATION_CREATED_DATE_INDEX].toString()));
            notification.setRoleTarget(Long.parseLong(data[NOTIFICATION_ROLE_TARGET_INDEX].toString()));
            result.add(notification);
        }
        return result;
    }

    //CAST OBJECT TO OBJECT DEFINED
    public static Course castCourseCreateViewModelToCourse(CourseCreateViewModel courseCreateViewModel){
        Course course = new Course();
        if(courseCreateViewModel != null){
            course.setName(courseCreateViewModel.getName());
            course.setDescription(courseCreateViewModel.getDescription());
            course.setStatusId(Status.COURSE_STATUS_DRAFTED);
            course.setImage(courseCreateViewModel.getImage());
            course.setRequiredElo(courseCreateViewModel.getRequiredElo());
        }
        return course;
    }

    public static CourseDetailsViewModel castCourseToCourseDetailsViewModel(Course course,int totalLesson){
        CourseDetailsViewModel courseDetailsViewModel = new CourseDetailsViewModel();
        courseDetailsViewModel.setCourseId(course.getCourseId());
        courseDetailsViewModel.setName(course.getName());
        courseDetailsViewModel.setDescription(course.getDescription());
        courseDetailsViewModel.setCreatedDate(course.getCreatedDate());
        courseDetailsViewModel.setStatusId(course.getStatusId());
        courseDetailsViewModel.setImage(course.getImage());
        courseDetailsViewModel.setUserEnrolleds(course.getUserEnrolleds());
        courseDetailsViewModel.setTutors(course.getTutors());
        courseDetailsViewModel.setListCategorys(course.getListCategorys());
        courseDetailsViewModel.setLessonViewModels(course.getLessonViewModels());
        courseDetailsViewModel.setListLearningLogLessonIds(course.getListLearningLogLessonIds());
        courseDetailsViewModel.setRequiredElo(course.getRequiredElo());
        UserDetailViewModel author = new UserDetailViewModel();
        author.setUserId(course.getUser().getUserId());
        author.setFullName(course.getUser().getFullName());
        author.setAvatar(course.getUser().getAvatar());

        courseDetailsViewModel.setAuthor(author);
        courseDetailsViewModel.setTotalLesson(totalLesson);
        courseDetailsViewModel.setListLogExerciseIds(course.getListLogExerciseIds());
        return courseDetailsViewModel;
    }

    public static User castUserUpdateToUser(UserUpdateViewModel userUpdateViewModel){
        User user = new User();
        if(userUpdateViewModel != null){
            user.setUserId(userUpdateViewModel.getUserId());
            user.setFullName(userUpdateViewModel.getFullName());
            user.setAvatar(userUpdateViewModel.getAvatar());
            user.setRoleId(userUpdateViewModel.getRoleId());
            user.setAchievement(userUpdateViewModel.getAchievement());
            List<Certificate> certificates = new ArrayList();
            for (CertificateUpdateViewModel c:
                 userUpdateViewModel.getCertificates()) {
                Certificate certificate = new Certificate();
                if(c.getCertificateId() != 0){
                    certificate.setCertificateId(c.getCertificateId());
                }
                certificate.setCertificateLink(c.getCertificateLink());
                certificates.add(certificate);
            }
            user.setCertificates(certificates);
        }
        return user;
    }

    public static User castUserRegisterToUser(UserRegisterViewModel userRegisterViewModel,User user){
        if(userRegisterViewModel != null){
            user.setUserId(userRegisterViewModel.getUserId());
            int eloSetting = EloRatingLevel.getEloById(userRegisterViewModel.getEloId());
            if(eloSetting == 0){
                eloSetting = EloRatingLevel.BEGINNER_ELO;
            }
            user.setPoint(eloSetting);
            user.setFullName(userRegisterViewModel.getFullName());
            user.setAvatar(userRegisterViewModel.getAvatar());
            user.setActive(userRegisterViewModel.isActive());
            user.setRoleId(userRegisterViewModel.getRoleId());
            user.setAchievement(userRegisterViewModel.getAchievement());
            List<Certificate> certificates = new ArrayList();
            for (CertificateUpdateViewModel c:
                    userRegisterViewModel.getCertificates()) {
                Certificate certificate = new Certificate();
                if(c.getCertificateId() != 0){
                    certificate.setCertificateId(c.getCertificateId());
                }
                certificate.setCertificateLink(c.getCertificateLink());
                certificates.add(certificate);
            }
            user.setCertificates(certificates);
        }
        return user;
    }

    public static List<ReviewPaginationViewModel> castListObjectToReviewFromGetReview(List<Object[]> objects)
    throws NumberFormatException{
        List<ReviewPaginationViewModel> data = new ArrayList<>();
        for (Object[] object:
                objects) {
            ReviewPaginationViewModel reviewPaginationViewModel = new ReviewPaginationViewModel();
            reviewPaginationViewModel.setReviewId(Long.parseLong(object[REVIEW_VIEW_MODEL_ID_INDEX].toString()));
            reviewPaginationViewModel.setRating(Integer.parseInt(object[REVIEW_VIEW_MODEL_RATING_INDEX].toString()));
            reviewPaginationViewModel.setContent(object[REVIEW_VIEW_MODEL_CONTENT_INDEX].toString());
            reviewPaginationViewModel.setCreatedDate(Timestamp.valueOf(object[REVIEW_VIEW_MODEL_CREATED_DATE_INDEX].toString()));
            if(object[REVIEW_VIEW_MODEL_MODIFIED_DATE_INDEX] != null){
                reviewPaginationViewModel.setModifiedDate(Timestamp.valueOf(object[REVIEW_VIEW_MODEL_MODIFIED_DATE_INDEX].toString()));
            }

            UserDetailViewModel reviewer = new UserDetailViewModel();
            reviewer.setUserId(Long.parseLong(object[REVIEW_VIEW_MODEL_USER_ID_INDEX].toString()));
            reviewer.setEmail(object[REVIEW_VIEW_MODEL_EMAIL_INDEX].toString());
            reviewer.setFullName(object[REVIEW_VIEW_MODEL_USER_FULLNAME_INDEX].toString());
            reviewer.setAvatar(object[REVIEW_VIEW_MODEL_USER_AVATAR_INDEX].toString());
            reviewPaginationViewModel.setReviewer(reviewer);

            data.add(reviewPaginationViewModel);
        }
        return data;
    }

    public static List<CourseForNotificationViewModel> castListObjectsToCourseForNotificationViewModel(List<Object[]> objects){
        List<CourseForNotificationViewModel> data = new ArrayList<>();
        for(Object[] object:
            objects){
            CourseForNotificationViewModel courseForNotificationViewModel = new CourseForNotificationViewModel();
            courseForNotificationViewModel.setCourseId(Long.parseLong(object[COURSE_FOR_NOTIFICATION_ID_INDEX].toString()));
            courseForNotificationViewModel.setCourseName(object[COURSE_FOR_NOTIFICATION_NAME_INDEX].toString());
            courseForNotificationViewModel.setCourseImage(object[COURSE_FOR_NOTIFICATION_IMAGE_INDEX].toString());

            data.add(courseForNotificationViewModel);
        }
        return data;
    }

    public static CourseForNotificationViewModel castObjectsToCourseForNotificationViewModel(Object objects){
        Object[] object = (Object[]) objects;
        if(object == null){
            return null;
        }
        CourseForNotificationViewModel courseForNotificationViewModel = new CourseForNotificationViewModel();
        courseForNotificationViewModel.setCourseId(Long.parseLong(object[COURSE_FOR_NOTIFICATION_ID_INDEX].toString()));
        courseForNotificationViewModel.setCourseName(object[COURSE_FOR_NOTIFICATION_NAME_INDEX].toString());
        courseForNotificationViewModel.setCourseImage(object[COURSE_FOR_NOTIFICATION_IMAGE_INDEX].toString());
        return courseForNotificationViewModel;
    }

    public static List<LearningLogViewModel> castListObjectsToLearningLogViewModel(List<Object[]> objects){
        List<LearningLogViewModel> data = new ArrayList<>();
        for(Object[] object:
                objects){
            LearningLogViewModel learningLogViewModel = new LearningLogViewModel();
            learningLogViewModel.setLearningLogId(Long.parseLong(object[LEARNINGLOG_ID_INDEX].toString()));
            learningLogViewModel.setPassed(parseBoolean(object[LEARNINGLOG_IS_PASSED_INDEX].toString()));
            data.add(learningLogViewModel);
        }
        return data;
    }
    //END CAST OBJECT TO OBJECT DEFINED

    //PRIVATE DEFINED
    private static String handleNullValueObject(Object object,Class clazz){
        if(object != null){
            return object.toString();
        }else if(clazz == String.class){
            return STRING_DEFAULT;
        }else if(clazz == Boolean.class){
            return BOOLEAN_DEFAULT;
        }else{
            return NUMBER_DEFAULT;
        }
    }

    private static boolean parseBoolean(String strBoolean){
        return strBoolean.equals("1") || strBoolean.toLowerCase().equals("true") ? true : false;
    }
    //END PRIVATE DEFINED
}
