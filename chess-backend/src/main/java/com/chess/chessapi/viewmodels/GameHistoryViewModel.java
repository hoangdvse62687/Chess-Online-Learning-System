package com.chess.chessapi.viewmodels;

import java.sql.Timestamp;

public class GameHistoryViewModel {
    private long gamehistoryId;
    private Timestamp startTime;
    private int level;
    private int gameTime;
    private float point;
    private int status;

    public long getGamehistoryId() {
        return gamehistoryId;
    }

    public void setGamehistoryId(long gamehistoryId) {
        this.gamehistoryId = gamehistoryId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getGameTime() {
        return gameTime;
    }

    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
    }

    public float getPoint() {
        return point;
    }

    public void setPoint(float point) {
        this.point = point;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
