package com.chess.chessapi.models;

public class UserSimilarSuggestionAlgorithm {
    private Long userId;
    private Double similarScore;

    public UserSimilarSuggestionAlgorithm(Long userId, Double similarScore) {
        this.userId = userId;
        this.similarScore = similarScore;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getSimilarScore() {
        return similarScore;
    }

    public void setSimilarScore(Double similarScore) {
        this.similarScore = similarScore;
    }
}
