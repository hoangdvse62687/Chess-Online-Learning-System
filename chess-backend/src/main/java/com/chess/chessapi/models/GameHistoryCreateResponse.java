package com.chess.chessapi.models;

public class GameHistoryCreateResponse {
    private long savedId;
    private boolean isSuccess;
    private PredictionEloStockfish predictionEloStockfish;

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

    public PredictionEloStockfish getPredictionEloStockfish() {
        return predictionEloStockfish;
    }

    public void setPredictionEloStockfish(PredictionEloStockfish predictionEloStockfish) {
        this.predictionEloStockfish = predictionEloStockfish;
    }
}
