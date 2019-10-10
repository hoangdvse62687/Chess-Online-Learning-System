package com.chess.chessapi.constants;

public class Status {
    //ACTIVE STATUS DEFINED
    public static final boolean ACTIVE = true;
    public static final boolean INACTIVE = false;
    //END ACTIVE STATUS DEFINED

    //COURSE STATUS DEFINED
    public static final long COURSE_STATUS_DRAFTED = 1;
    public static final long COURSE_STATUS_PUBLISHED = 2;
    public static final long COURSE_STATUS_REMOVED = 3;
    public static final long COURSE_STATUS_WAITING = 4;
    public static final long COURSE_STATUS_REJECTED = 5;
    //END COURSE STATUS DEFINED

    //USER HAS COURSE DEFINED
    public static final long USER_HAS_COURSE_STATUS_IN_PROCESS = 1;
    public static final long USER_HAS_COURSE_STATUS_PASSED = 2;
    public static final long USER_HAS_COURSE_STATUS_NOT_PASSED = 3;
    //END USER HAS COURSE DEFINED
}
