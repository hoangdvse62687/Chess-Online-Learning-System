package com.chess.chessapi.entities;

import com.chess.chessapi.constants.AuthProvider;
import com.chess.chessapi.viewmodels.CourseDetailViewModel;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="userId",scope = User.class)
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "getUsersByCourseid",
                procedureName = "get_users_by_courseid",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "courseId",type = Long.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "getUsersRegisterReport",
                procedureName = "get_users_register_report",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "year",type = Integer.class)
                }
        )
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long userId;

    @Email
    @Length(max = 255, message = "Email shouldn't larger than 255 characters")
    @NotNull(message = "Email is required not null")
    private String email;

    @NotNull(message = "Full Name is required not null")
    @Length(min = 6,max = 255, message = "Full name should be in range 6~255 characters")
    @Column(name = "full_name")
    private String fullName;


    @Length(max = 255, message = "Link avatar shouldn't larger than 255 characters")
    private String avatar;

    @Column(name = "created_date")
    private java.sql.Timestamp createdDate;

    @Column(name = "is_active")
    private boolean isActive;

    private int point;

    @Column(name = "role_id")
    private long roleId;

    @Column(name = "is_reviewed")
    private boolean isReviewed;

    @Column(name = "modified_date")
    private java.sql.Timestamp modifiedDate;

    @Length(max = 255, message = "Achievement shouldn't larger than 255 characters")
    private String achievement;

    @OneToMany( fetch = FetchType.LAZY,mappedBy = "user")
    private List<Certificate> certificates;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user")
    @JsonIgnore
    private List<UserHasCourse> userHasCourses;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user")
    @JsonIgnore
    private List<Lesson> lessons;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user")
    @JsonIgnore
    private List<Course> courses;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user")
    @JsonIgnore
    private List<LearningLog> learningLogs;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user")
    @JsonIgnore
    private List<Review> reviews;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user")
    @JsonIgnore
    private List<GameHistory> gameHistories;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user")
    @JsonIgnore
    private List<Notification> notifications;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user")
    @JsonIgnore
    private List<PointLog> pointLogs;

    @Transient
    private List<CourseDetailViewModel> courseDetailViewModels;

    @NotNull(message = "Provider mut not be null")
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Column(name = "provider_id")
    private String providerId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public List<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<Certificate> certificates) {
        this.certificates = certificates;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    @JsonIgnore
    public List<UserHasCourse> getUserHasCourses() {
        return userHasCourses;
    }

    public void setUserHasCourses(List<UserHasCourse> userHasCourses) {
        this.userHasCourses = userHasCourses;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public List<CourseDetailViewModel> getCourseDetailViewModels() {
        return courseDetailViewModels;
    }

    public void setCourseDetailViewModels(List<CourseDetailViewModel> courseDetailViewModels) {
        this.courseDetailViewModels = courseDetailViewModels;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<LearningLog> getLearningLogs() {
        return learningLogs;
    }

    public void setLearningLogs(List<LearningLog> learningLogs) {
        this.learningLogs = learningLogs;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<GameHistory> getGameHistories() {
        return gameHistories;
    }

    public void setGameHistories(List<GameHistory> gameHistories) {
        this.gameHistories = gameHistories;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public boolean isReviewed() {
        return isReviewed;
    }

    public void setReviewed(boolean reviewed) {
        isReviewed = reviewed;
    }

    public List<PointLog> getPointLogs() {
        return pointLogs;
    }

    public void setPointLogs(List<PointLog> pointLogs) {
        this.pointLogs = pointLogs;
    }

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
