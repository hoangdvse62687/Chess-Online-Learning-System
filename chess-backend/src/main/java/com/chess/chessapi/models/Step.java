package com.chess.chessapi.models;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

public class Step {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
