package com.chess.chessapi.constants;

import java.io.Serializable;

public class AppMessage implements Serializable {
    //ACTION DEFINE
    public static final String UPDATE = "Update";
    public static final String CREATE = "Create";
    public static final String DELETE = "Delete";
    //END ACTION DEFINE

    //SPECIAL DEFINE
    public static final String USER = "user";
    public static final String PROFILE = "profile";
    public static final String COURSE = "course";
    public static final String CATEGORY = "category";
    public static final String LESSON = "lesson";
    public static final String INTERACTIVE_LESSON = "interactive lesson";
    public static final String UNINTERACTIVE_LESSON = "uninteractive lesson";
    public static final String LEARNING_LOG = "learning log";
    public static final String EXERCISE = "exercise";
    public static final String ENROLL = "enroll";
    public static final String RESOURCE_LINK = "resource link";
    public static final String REVIEW = "review";
    public static final String GAME_HISTORY = "game history";
    public static final String NOTIFICATION = "notification";
    public static final String EXERCISE_LOG = "excercise log";
    //END SPECIAL DEFINE

    //STATUS MESSAGE DEFINE
    public static final String SUCCESSFUL_MESSAGE = "successful";
    public static final String FAIL_MESSAGE = "fail";
    //END STATUS MESSAGE DEFINE

    //PERMISSION DEFINE
    public static final String PERMISSION_DENY_MESSAGE = "you don't have permission to do this action";
    public static final String ELO_DENY_MESSAGE = "your point currently don't have enough to do this action";
    public static final String NOT_ACCEPTED_MESSAGE = "Bạn đã comment khóa học này";
    //END PERMISION DEFINE

    //NOTIFICATION DEFINE
    public static final String CREATE_NEW_USER_AS_INSTRUCTOR = " đã tạo tài khoản giảng viên";
    public static final String UPDATE_USER_STATUS_ACTIVE = "đã được mở khóa";
    public static final String UPDATE_USER_STATUS_INACTIVE = "đã bị ngưng hoạt động";
    public static final String CREATE_NEW_COURSE = "đang chờ bạn xét duyệt";
    public static final String UPDATE_COURSE_STATUS_PUBLISHED = "đã được công khai";
    public static final String UPDATE_COURSE_STATUS_REJECTED = "đã bị từ chối công khai";
    public static final String NOTIFICATION_REVIEW = "đã nhận xét khóa học của bạn";
    //END NOTIFICATION DEFINE

    //MAIL DEFINED
    public static final String ACCEPT_REQUEST_SUBJECT = "Account Active";
    public static final String ACCEPT_REQUEST_CONTENT = "Chúc Mừng. Tài khoản của bạn đã được kich hoat!";
    public static final String REJECT_REQUEST_SUBJECT = "Account Inactive";
    public static final String REJECT_REQUEST_CONTENT = "Tài khoản của bạn đã bi ngung hoat dong!";
    public static final String PUBLISH_COURSE_REQUEST_SUBJECT  = " Course Published";
    public static final String PUBLISH_COURSE_REQUEST_CONTENT_PUBLISH = " đã được publish trên trang chủ.";
    public static final String PUBLISH_COURSE_REQUEST_CONTENT_REJECT = " đã bị từ chối publish trên trang chủ." +
            "<br/><strong>Lí do:</strong>";
    //END MAIL DEFINED

    public static String getMessageSuccess(String action,String table){
        return action + " " + table  + " " + SUCCESSFUL_MESSAGE;
    }

    public static String getMessageFail(String action,String table){
        return action + " " + table + " " + FAIL_MESSAGE;
    }
}
