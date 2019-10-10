package com.chess.chessapi.models;

public class PredictionEloStockfish {
    private int predictionWinningElo;
    private int predictionLoseElo;
    private int predictionDrawnElo;

    public int getPredictionWinningElo() {
        return predictionWinningElo;
    }

    public void setPredictionWinningElo(int predictionWinningElo) {
        this.predictionWinningElo = predictionWinningElo;
    }

    public int getPredictionLoseElo() {
        return predictionLoseElo;
    }

    public void setPredictionLoseElo(int predictionLoseElo) {
        this.predictionLoseElo = predictionLoseElo;
    }

    public int getPredictionDrawnElo() {
        return predictionDrawnElo;
    }

    public void setPredictionDrawnElo(int predictionDrawnElo) {
        this.predictionDrawnElo = predictionDrawnElo;
    }
}
