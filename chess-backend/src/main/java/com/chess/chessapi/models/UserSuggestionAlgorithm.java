package com.chess.chessapi.models;

import java.util.ArrayList;
import java.util.List;

public class UserSuggestionAlgorithm {
    private Long userId;
    private List<CourseUserFilterData> reviewSuggestionAlgorithms;
    private List<UserSimilarSuggestionAlgorithm> userSimilarSuggestionAlgorithms;
    private List<CourseUserFilterData> userFilterScore;

    public UserSuggestionAlgorithm(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<CourseUserFilterData> getReviewSuggestionAlgorithms() {
        if(this.reviewSuggestionAlgorithms == null){
            this.reviewSuggestionAlgorithms = new ArrayList<>();
        }
        return reviewSuggestionAlgorithms;
    }

    public void setReviewSuggestionAlgorithms(List<CourseUserFilterData> reviewSuggestionAlgorithms) {
        this.reviewSuggestionAlgorithms = reviewSuggestionAlgorithms;
    }

    public List<UserSimilarSuggestionAlgorithm> getUserSimilarSuggestionAlgorithms() {
        if(this.userSimilarSuggestionAlgorithms == null){
            this.userSimilarSuggestionAlgorithms = new ArrayList<>();
        }
        return userSimilarSuggestionAlgorithms;
    }

    public void setUserSimilarSuggestionAlgorithms(List<UserSimilarSuggestionAlgorithm> userSimilarSuggestionAlgorithms) {
        this.userSimilarSuggestionAlgorithms = userSimilarSuggestionAlgorithms;
    }

    public List<CourseUserFilterData> getUserFilterScore() {
        if(this.userFilterScore == null){
            this.userFilterScore = new ArrayList<>();
        }
        return userFilterScore;
    }

    public void setUserFilterScore(List<CourseUserFilterData> userFilterScore) {
        this.userFilterScore = userFilterScore;
    }
}
