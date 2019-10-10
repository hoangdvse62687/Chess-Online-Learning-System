package com.chess.chessapi.viewmodels;

public class LearningLogUpdateResponse {
    private boolean isSuccess;
    private boolean isComplete;

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
