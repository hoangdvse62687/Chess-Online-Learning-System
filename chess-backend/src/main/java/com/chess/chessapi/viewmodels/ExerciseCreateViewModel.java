package com.chess.chessapi.viewmodels;

import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ExerciseCreateViewModel {

    @NotNull(message = "Question must not be null")
    @Length(min=6,max = 1000,message = "Question is required in range 6~1000 characters")
    private String question;

    @NotNull(message = "Answer must not be null")
    @Valid
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
}
