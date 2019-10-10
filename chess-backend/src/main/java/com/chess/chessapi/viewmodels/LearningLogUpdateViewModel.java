package com.chess.chessapi.viewmodels;

public class LearningLogUpdateViewModel {
    private long learningLogId;
    private boolean isPassed;

    public long getLearningLogId() {
        return learningLogId;
    }

    public void setLearningLogId(long learningLogId) {
        this.learningLogId = learningLogId;
    }

    public boolean isPassed() {
        return isPassed;
    }

    public void setPassed(boolean passed) {
        isPassed = passed;
    }
}
