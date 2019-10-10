package com.chess.chessapi.models;

import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import java.io.Serializable;

public class CategoryRedis extends JdkSerializationRedisSerializer implements Serializable {
    private long categoryId;
    private String name;
    private static final long serialVersionUID = 1L;

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
