package com.chess.chessapi.viewmodels;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class LearningLogCreateViewModel {
    @NotNull(message = "Course id must not be null")
    private long courseId;

    @NotNull(message = "Lesson id must not be null")
    private long lessonId;

    private boolean isPassed;

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public long getLessonId() {
        return lessonId;
    }

    public void setLessonId(long lessonId) {
        this.lessonId = lessonId;
    }

    public boolean isPassed() {
        return isPassed;
    }

    public void setPassed(boolean passed) {
        isPassed = passed;
    }
}
