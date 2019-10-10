package com.chess.chessapi.viewmodels;

import com.chess.chessapi.models.StepSuggest;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

public class ExerciseAnswerArray {
    private String fen;
    private int answerType;

    @Valid
    private List<List<StepSuggest>> answerArr = new ArrayList<List<StepSuggest>>();

    public String getFen() {
        return fen;
    }

    public void setFen(String fen) {
        this.fen = fen;
    }

    public int getAnswerType() {
        return answerType;
    }

    public void setAnswerType(int answerType) {
        this.answerType = answerType;
    }

    public List<List<StepSuggest>> getAnswerArr() {
        return answerArr;
    }

    public void setAnswerArr(List<List<StepSuggest>> answerArr) {
        this.answerArr = answerArr;
    }
}
