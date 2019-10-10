package com.chess.chessapi.viewmodels;

import javax.validation.constraints.NotNull;

public class EnrollCourseViewModel {
    @NotNull(message = "Course Id must not be null")
    private long courseId;

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }
}
