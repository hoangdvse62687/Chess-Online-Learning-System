package com.chess.chessapi.viewmodels;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ReviewCreateViewModel {
    @NotNull(message = "Rating must not be null")

    @Min(value = 0,message = "Rating must larger than 0")
    @Max(value = 5,message = "Rating must lower or equal than 5")
    private int rating;

    @Length(min = 6,max = 1000,message = "name is required in range 6 ~ 1000 characters")
    @NotNull(message = "Content must not be null")
    private String content;

    @NotNull(message = "Course id must not be null")
    private long courseId;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }
}
