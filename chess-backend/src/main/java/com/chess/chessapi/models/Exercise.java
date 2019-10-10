package com.chess.chessapi.models;

import com.chess.chessapi.interfaces.LessonInterface;
import com.chess.chessapi.viewmodels.ExerciseAnswerArray;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class Exercise implements LessonInterface<Exercise> {

    @NotNull(message = "Question must not be null")
    @Length(max = 1000,message = "Question is required not larger than 1000 characters")
    private String question;

    @NotNull(message = "Steps must not be null")
    private ExerciseAnswerArray answer = new ExerciseAnswerArray();

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ExerciseAnswerArray getAnswer() {
        return answer;
    }

    public void setAnswer(ExerciseAnswerArray answer) {
        this.answer = answer;
    }

    @Override
    @JsonIgnore
    public Exercise getContent(String content) {
        Gson gson = new Gson();
        return gson.fromJson(content,Exercise.class);
    }

    @Override
    @JsonIgnore
    public String toJson(Exercise entity) {
        Gson gson = new Gson();
        return gson.toJson(entity);
    }
}
