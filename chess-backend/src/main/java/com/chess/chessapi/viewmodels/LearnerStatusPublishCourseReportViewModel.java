package com.chess.chessapi.viewmodels;

public class LearnerStatusPublishCourseReportViewModel {
    private String courseName;
    private int courseStatus;
    private int counterInProcessStatus;
    private int counterPassedStatus;
    private int counterNotPassedStatus;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCounterInProcessStatus() {
        return counterInProcessStatus;
    }

    public void setCounterInProcessStatus(int counterInProcessStatus) {
        this.counterInProcessStatus = counterInProcessStatus;
    }

    public int getCounterPassedStatus() {
        return counterPassedStatus;
    }

    public void setCounterPassedStatus(int counterPassedStatus) {
        this.counterPassedStatus = counterPassedStatus;
    }

    public int getCounterNotPassedStatus() {
        return counterNotPassedStatus;
    }

    public void setCounterNotPassedStatus(int counterNotPassedStatus) {
        this.counterNotPassedStatus = counterNotPassedStatus;
    }

    public int getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(int courseStatus) {
        this.courseStatus = courseStatus;
    }
}
