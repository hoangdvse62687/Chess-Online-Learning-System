package com.chess.chessapi.viewmodels;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class GameHistoryCreateViewModel {

    @NotNull(message = "Level must not be null")
    private int level;

    @NotNull(message = "Color must not be null")
    private int color;

    @NotNull(message = "Game time must not be null")
    @Column(name = "game_time")
    private int gameTime;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getGameTime() {
        return gameTime;
    }

    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
    }
}
