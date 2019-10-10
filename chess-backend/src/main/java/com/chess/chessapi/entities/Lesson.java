package com.chess.chessapi.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "lesson")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="lessonId",scope = Lesson.class)
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "getLessonByCourseId",
                procedureName = "get_lesson_by_courseid",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "courseId",type = Long.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "checkPermssionToViewLesson",
                procedureName = "check_permssion_to_view_lesson",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "userId",type = Long.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "lessonId",type = Long.class),
                        @StoredProcedureParameter(mode = ParameterMode.INOUT,name = "hasPermission",type = Boolean.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "getLessonPaginationByUserid",
                procedureName = "get_lesson_pagination_by_userid",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "userId",type = Long.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "lessonName",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "pageIndex",type = Integer.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "pageSize",type = Integer.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "sortBy",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "sortDirection",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.INOUT,name = "totalElements",type = Long.class)
                }
        )
})
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long lessonId;

    @NotNull(message = "Name must not be null")
    @Length(max = 1000,message = "name is required not large than 1000 characters")
    private String name;

    @Length(max = 1000,message = "Description is required not large than 1000 characters")
    private String description;

    @NotNull(message = "Content must not be null")
    @JsonIgnore
    private String content;

    @Transient
    private Object lessonContent;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "lesson")
    @JsonIgnore
    private List<CourseHasLesson> courseHasLessons;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "lesson")
    @JsonIgnore
    private List<LearningLog> learningLogs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="owner")
    @JsonIgnore
    private User user;

    @Column(name = "type")
    private int lessonType;

    @Column(name = "modified_date")
    private java.sql.Timestamp modifiedDate;

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

    public List<CourseHasLesson> getCourseHasLessons() {
        return courseHasLessons;
    }

    public void setCourseHasLessons(List<CourseHasLesson> courseHasLessons) {
        this.courseHasLessons = courseHasLessons;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<LearningLog> getLearningLogs() {
        return learningLogs;
    }

    public void setLearningLogs(List<LearningLog> learningLogs) {
        this.learningLogs = learningLogs;
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

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object getLessonContent() {
        return lessonContent;
    }

    public void setLessonContent(Object lessonContent) {
        this.lessonContent = lessonContent;
    }
}
