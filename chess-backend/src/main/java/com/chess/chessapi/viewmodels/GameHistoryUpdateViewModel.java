package com.chess.chessapi.viewmodels;

import javax.validation.constraints.NotNull;

public class GameHistoryUpdateViewModel {
    private long gameHistoryId;

    @NotNull(message = "Record must not be null")
    private String record;

    @NotNull(message = "Point must not be null")
    private float point;

    private int status;

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
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

    public long getGameHistoryId() {
        return gameHistoryId;
    }

    public void setGameHistoryId(long gameHistoryId) {
        this.gameHistoryId = gameHistoryId;
    }
}
