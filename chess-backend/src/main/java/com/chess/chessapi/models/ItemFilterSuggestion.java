package com.chess.chessapi.models;

public class ItemFilterSuggestion {
    private int sameAuthor;
    private double sameCategories;
    private double rating;

    public int getSameAuthor() {
        return sameAuthor;
    }

    public void setSameAuthor(int sameAuthor) {
        this.sameAuthor = sameAuthor;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getSameCategories() {
        return sameCategories;
    }

    public void setSameCategories(double sameCategories) {
        this.sameCategories = sameCategories;
    }
}
