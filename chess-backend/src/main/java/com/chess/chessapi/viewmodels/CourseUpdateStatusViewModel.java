package com.chess.chessapi.viewmodels;

import javax.validation.constraints.NotNull;

public class CourseUpdateStatusViewModel {
    @NotNull(message = "Course Id must not be null")
    private long courseId;
    @NotNull(message = "Status Id must not be null")
    private long statusId;

    private String reasonReject;

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public long getStatusId() {
        return statusId;
    }

    public void setStatusId(long statusId) {
        this.statusId = statusId;
    }

    public String getReasonReject() {
        return reasonReject;
    }

    public void setReasonReject(String reasonReject) {
        this.reasonReject = reasonReject;
    }
}
