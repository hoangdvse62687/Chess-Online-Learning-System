package com.chess.chessapi.models;

import com.chess.chessapi.interfaces.LessonInterface;
import com.google.gson.Gson;

public class UninteractiveLesson implements LessonInterface<UninteractiveLesson> {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public UninteractiveLesson getContent(String content) {
        Gson gson = new Gson();
        return gson.fromJson(content,UninteractiveLesson.class);
    }

    @Override
    public String toJson(UninteractiveLesson entity) {
        Gson gson = new Gson();
        return gson.toJson(entity);
    }
}
