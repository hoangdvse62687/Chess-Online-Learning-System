package com.chess.chessapi.models;

import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommonCourseItemSuggestionRedis extends JdkSerializationRedisSerializer implements Serializable {
    private long courseId;
    private List<CourseUserFilterData> courseItemFilterData;
    private static final long serialVersionUID = 1L;

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public List<CourseUserFilterData> getCourseItemFilterData() {
        if(this.courseItemFilterData == null){
            this.courseItemFilterData = new ArrayList<>();
        }
        return courseItemFilterData;
    }

    public void setCourseItemFilterData(List<CourseUserFilterData> courseItemFilterData) {
        this.courseItemFilterData = courseItemFilterData;
    }
}
