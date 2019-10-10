package com.chess.chessapi.viewmodels;

import java.sql.Timestamp;

public class ReviewPaginationViewModel {
    private long reviewId;

    private int rating;

    private String content;

    public UserDetailViewModel reviewer;

    private Timestamp createdDate;

    private Timestamp modifiedDate;

    public void setRating(int rating) {
        this.rating = rating;
    }

    public float getRating() {
        return rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserDetailViewModel getReviewer() {
        return reviewer;
    }

    public void setReviewer(UserDetailViewModel reviewer) {
        this.reviewer = reviewer;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public long getReviewId() {
        return reviewId;
    }

    public void setReviewId(long reviewId) {
        this.reviewId = reviewId;
    }

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
