package com.chess.chessapi.viewmodels;

import java.sql.Timestamp;
import java.util.List;

public class CourseDetailsViewModel {
    private long courseId;
    private String name;
    private String description;
    private Timestamp createdDate;
    private int requiredElo;
    private Long statusId;
    private String image;
    private UserDetailViewModel author;
    private List<UserDetailViewModel> userEnrolleds;
    private List<UserDetailViewModel> tutors;
    private List<CategoryViewModel> listCategorys;
    private List<LearningLogViewModel> listLearningLogLessonIds;
    private List<LessonViewModel> lessonViewModels;
    private int totalLesson;
    private boolean isEnrolled;
    private List<Long> listLogExerciseIds;
    private boolean isCommented;

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<CategoryViewModel> getListCategorys() {
        return listCategorys;
    }

    public void setListCategorys(List<CategoryViewModel> listCategorys) {
        this.listCategorys = listCategorys;
    }

    public List<LessonViewModel> getLessonViewModels() {
        return lessonViewModels;
    }

    public void setLessonViewModels(List<LessonViewModel> lessonViewModels) {
        this.lessonViewModels = lessonViewModels;
    }

    public boolean isEnrolled() {
        return isEnrolled;
    }

    public void setEnrolled(boolean enrolled) {
        isEnrolled = enrolled;
    }

    public List<LearningLogViewModel> getListLearningLogLessonIds() {
        return listLearningLogLessonIds;
    }

    public void setListLearningLogLessonIds(List<LearningLogViewModel> listLearningLogLessonIds) {
        this.listLearningLogLessonIds = listLearningLogLessonIds;
    }

    public UserDetailViewModel getAuthor() {
        return author;
    }

    public void setAuthor(UserDetailViewModel author) {
        this.author = author;
    }

    public int getTotalLesson() {
        return totalLesson;
    }

    public void setTotalLesson(int totalLesson) {
        this.totalLesson = totalLesson;
    }

    public List<UserDetailViewModel> getUserEnrolleds() {
        return userEnrolleds;
    }

    public void setUserEnrolleds(List<UserDetailViewModel> userEnrolleds) {
        this.userEnrolleds = userEnrolleds;
    }

    public List<UserDetailViewModel> getTutors() {
        return tutors;
    }

    public void setTutors(List<UserDetailViewModel> tutors) {
        this.tutors = tutors;
    }

    public List<Long> getListLogExerciseIds() {
        return listLogExerciseIds;
    }

    public void setListLogExerciseIds(List<Long> listLogExerciseIds) {
        this.listLogExerciseIds = listLogExerciseIds;
    }

    public int getRequiredElo() {
        return requiredElo;
    }

    public void setRequiredElo(int requiredElo) {
        this.requiredElo = requiredElo;
    }

    public boolean isCommented() {
        return isCommented;
    }

    public void setCommented(boolean commented) {
        isCommented = commented;
    }
}
