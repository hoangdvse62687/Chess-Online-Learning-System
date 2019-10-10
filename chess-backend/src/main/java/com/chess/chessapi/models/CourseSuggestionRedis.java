package com.chess.chessapi.models;

import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CourseSuggestionRedis extends JdkSerializationRedisSerializer implements Serializable {
    private long userId;
    private List<CourseUserFilterData> courseUserFilterData;
    private List<CourseUserFilterData> courseItemFilterData;
    private boolean isUsedCourseItemFilterData;
    private static final long serialVersionUID = 1L;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public List<CourseUserFilterData> getCourseUserFilterData() {
        if(this.courseUserFilterData == null){
            this.courseUserFilterData = new ArrayList<>();
        }
        return courseUserFilterData;
    }

    public void setCourseUserFilterData(List<CourseUserFilterData> courseUserFilterData) {
        this.courseUserFilterData = courseUserFilterData;
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

    public boolean isUsedCourseItemFilterData() {
        return isUsedCourseItemFilterData;
    }

    public void setUsedCourseItemFilterData(boolean usedCourseItemFilterData) {
        isUsedCourseItemFilterData = usedCourseItemFilterData;
    }
}
