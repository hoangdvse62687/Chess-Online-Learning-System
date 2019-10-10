package com.chess.chessapi.viewmodels;

public class LearningLogCreateResponse {
    private long savedId;
    private boolean isSuccess;
    private boolean isComplete;

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

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
