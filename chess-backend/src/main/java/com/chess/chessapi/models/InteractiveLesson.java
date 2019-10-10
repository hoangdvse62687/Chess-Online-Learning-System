package com.chess.chessapi.models;

import com.chess.chessapi.interfaces.LessonInterface;
import com.google.gson.Gson;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class InteractiveLesson implements LessonInterface<InteractiveLesson> {
    @NotNull(message = "Init code must not be null")
    @Length(max = 1000,message = "InitCode is required not larger than 1000 characters")
    private String initCode;

    @NotNull(message = "Steps must not be null")
    private List<@NotNull(message = "Step must not be null") Step> steps = new ArrayList<Step>();

    public String getInitCode() {
        return initCode;
    }

    public void setInitCode(String initCode) {
        this.initCode = initCode;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @Override
    public InteractiveLesson getContent(String content) {
        Gson gson = new Gson();
        return gson.fromJson(content,InteractiveLesson.class);
    }

    @Override
    public String toJson(InteractiveLesson entity) {
        Gson gson = new Gson();
        return gson.toJson(entity);
    }
}
