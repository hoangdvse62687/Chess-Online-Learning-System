package com.chess.chessapi.models;

public class ChessGameInitResponse {
    private boolean isReconnect;
    private boolean isConnectSuccess;
    private ChessGame chessGame;

    public boolean isReconnect() {
        return isReconnect;
    }

    public void setReconnect(boolean reconnect) {
        isReconnect = reconnect;
    }

    public ChessGame getChessGame() {
        return chessGame;
    }

    public void setChessGame(ChessGame chessGame) {
        this.chessGame = chessGame;
    }

    public boolean isConnectSuccess() {
        return isConnectSuccess;
    }

    public void setConnectSuccess(boolean connectSuccess) {
        isConnectSuccess = connectSuccess;
    }
}
