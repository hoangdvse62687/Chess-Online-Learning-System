package com.chess.chessapi.models;

import java.io.Serializable;
import java.sql.Timestamp;

public class ChessGame implements Serializable {

    private Long gameHistoryId;
    private int level;
    private int color;
    private String gameContent;
    private int status;
    private int secondGameTime;
    private Timestamp lastAccessed;
    private int nextTurnPlayer;
    private String currentFen;
    private PlayerInfo player1;
    private PlayerInfo player2;
    private PredictionEloStockfish predictionEloStockfish;

    public Long getGameHistoryId() {
        return gameHistoryId;
    }

    public void setGameHistoryId(Long gameHistoryId) {
        this.gameHistoryId = gameHistoryId;
    }

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

    public String getGameContent() {
        return gameContent;
    }

    public void setGameContent(String gameContent) {
        this.gameContent = gameContent;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSecondGameTime() {
        return secondGameTime;
    }

    public void setSecondGameTime(int secondGameTime) {
        this.secondGameTime = secondGameTime;
    }

    public Timestamp getLastAccessed() {
        return lastAccessed;
    }

    public void setLastAccessed(Timestamp lastAccessed) {
        this.lastAccessed = lastAccessed;
    }

    public int getNextTurnPlayer() {
        return nextTurnPlayer;
    }

    public void setNextTurnPlayer(int nextTurnPlayer) {
        this.nextTurnPlayer = nextTurnPlayer;
    }

    public PlayerInfo getPlayer1() {
        return player1;
    }

    public void setPlayer1(PlayerInfo player1) {
        this.player1 = player1;
    }

    public PlayerInfo getPlayer2() {
        return player2;
    }

    public void setPlayer2(PlayerInfo player2) {
        this.player2 = player2;
    }

    public String getCurrentFen() {
        return currentFen;
    }

    public void setCurrentFen(String currentFen) {
        this.currentFen = currentFen;
    }

    public PredictionEloStockfish getPredictionEloStockfish() {
        return predictionEloStockfish;
    }

    public void setPredictionEloStockfish(PredictionEloStockfish predictionEloStockfish) {
        this.predictionEloStockfish = predictionEloStockfish;
    }
}
