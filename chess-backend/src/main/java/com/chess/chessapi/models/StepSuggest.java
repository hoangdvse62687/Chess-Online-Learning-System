package com.chess.chessapi.models;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class StepSuggest {
    private int id;

    @Length(max = 1000,message = "Move is required not large than 1000 characters")
    private String move;

    @Length(max = 1000,message = "Content is required not large than 1000 characters")
    private String content;

    @Length(max = 1000,message = "Move direction is required not large than 1000 characters")
    private String moveDirection;

    @Length(max = 1000,message = "Fen is required not large than 1000 characters")
    private String fen;

    private int preId;

    @Length(max = 1000,message = "right_response is required not large than 255 characters")
    private String rightResponse;

    @Length(max = 1000,message = "wrong_response is required not large than 255 characters")
    private String wrongResponse;

    private String suggest;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRightResponse() {
        return rightResponse;
    }

    public void setRightResponse(String rightResponse) {
        this.rightResponse = rightResponse;
    }

    public String getWrongResponse() {
        return wrongResponse;
    }

    public void setWrongResponse(String wrongResponse) {
        this.wrongResponse = wrongResponse;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public String getMoveDirection() {
        return moveDirection;
    }

    public void setMoveDirection(String moveDirection) {
        this.moveDirection = moveDirection;
    }

    public String getFen() {
        return fen;
    }

    public void setFen(String fen) {
        this.fen = fen;
    }

    public int getPreId() {
        return preId;
    }

    public void setPreId(int preId) {
        this.preId = preId;
    }
}
