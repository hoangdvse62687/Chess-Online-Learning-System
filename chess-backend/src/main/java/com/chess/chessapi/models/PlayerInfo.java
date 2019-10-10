package com.chess.chessapi.models;

public class PlayerInfo {
    private boolean isBot;
    private long secondCountDown;

    public boolean isBot() {
        return isBot;
    }

    public void setBot(boolean bot) {
        isBot = bot;
    }

    public long getSecondCountDown() {
        return secondCountDown;
    }

    public void setSecondCountDown(long secondCountDown) {
        this.secondCountDown = secondCountDown;
    }
}
