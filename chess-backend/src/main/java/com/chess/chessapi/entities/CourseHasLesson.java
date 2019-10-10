package com.chess.chessapi.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

@Entity
@Table(name = "course_has_lesson")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="courseHasLessonId",scope = CourseHasLesson.class)
public class CourseHasLesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long courseHasLessonId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="lesson_id")
    @JsonIgnore
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="course_id")
    @JsonIgnore
    private Course course;

    @Column(name = "lesson_ordered")
    private int lessonOrdered;

    public long getCourseHasLessonId() {
        return courseHasLessonId;
    }

    public void setCourseHasLessonId(long courseHasLessonId) {
        this.courseHasLessonId = courseHasLessonId;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getLessonOrdered() {
        return lessonOrdered;
    }

    public void setLessonOrdered(int lessonOrdered) {
        this.lessonOrdered = lessonOrdered;
    }
}
