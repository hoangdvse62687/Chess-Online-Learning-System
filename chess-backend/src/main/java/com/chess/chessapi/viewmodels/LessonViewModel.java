package com.chess.chessapi.viewmodels;

import java.sql.Timestamp;

public class LessonViewModel {
    private long lessonId;
    private String name;
    private String description;
    private Timestamp createdDate;
    private int lessonOrdered;
    private int lessonType;
    public long getLessonId() {
        return lessonId;
    }

    public void setLessonId(long lessonId) {
        this.lessonId = lessonId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public int getLessonOrdered() {
        return lessonOrdered;
    }

    public void setLessonOrdered(int lessonOrdered) {
        this.lessonOrdered = lessonOrdered;
    }

    public int getLessonType() {
        return lessonType;
    }

    public void setLessonType(int lessonType) {
        this.lessonType = lessonType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
