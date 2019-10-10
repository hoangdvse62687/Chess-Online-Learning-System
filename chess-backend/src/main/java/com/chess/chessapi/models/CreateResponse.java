package com.chess.chessapi.models;

public class CreateResponse {
    private long savedId;
    private boolean isSuccess;

    public long getSavedId() {
        return savedId;
    }

    public void setSavedId(long savedId) {
        this.savedId = savedId;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
