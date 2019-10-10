package com.chess.chessapi.models;

import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import java.io.Serializable;

public class CourseUserFilterData extends JdkSerializationRedisSerializer implements Serializable,Comparable<CourseUserFilterData> {
    private Long courseId;
    private Double score;
    private static final long serialVersionUID = 1L;

    public CourseUserFilterData() {
    }

    public CourseUserFilterData(Long courseId, Double score) {
        this.courseId = courseId;
        this.score = score;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public int compareTo(CourseUserFilterData o) {
        if(this.score == o.getScore()){
            return 0;
        }else if(this.score < o.getScore()){
            return 1;
        }else{
            return -1;
        }
    }
}
