package com.chess.chessapi.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Year;

@Entity
@Table(name = "user_has_course")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="userHasCourseId",scope = UserHasCourse.class)
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "getLearnerStatusCourseReport",
                procedureName = "get_learner_status_course_report",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "authorId",type = Long.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "pageIndex",type = Integer.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "pageSize",type = Integer.class),
						@StoredProcedureParameter(mode = ParameterMode.IN,name = "courseName",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "sortBy",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "sortDirection",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.INOUT,name = "totalElements",type = Long.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "getEnrollmentReport",
                procedureName = "get_enrollment_report",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "authorId",type = Long.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "year",type = Integer.class)
                }
        )
})
public class UserHasCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long userHasCourseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="course_id")
    @JsonIgnore
    private Course course;

    @Column(name = "enrolled_date")
    private Timestamp enrolledDate;

    @Column(name = "modified_date")
    private java.sql.Timestamp modifiedDate;

    @Column(name = "status_id")
    private long statusId;

    public long getUserHasCourseId() {
        return userHasCourseId;
    }

    public void setUserHasCourseId(long userHasCourseId) {
        this.userHasCourseId = userHasCourseId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Timestamp getEnroledDate() {
        return enrolledDate;
    }

    public void setEnroledDate(Timestamp enrolledDate) {
        this.enrolledDate = enrolledDate;
    }

    public long getStatusId() {
        return statusId;
    }

    public void setStatusId(long statusId) {
        this.statusId = statusId;
    }

    public Timestamp getEnrolledDate() {
        return enrolledDate;
    }

    public void setEnrolledDate(Timestamp enrolledDate) {
        this.enrolledDate = enrolledDate;
    }

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
