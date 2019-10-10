package com.chess.chessapi.viewmodels;

import java.util.ArrayList;
import java.util.List;

public class CourseOverviewViewModel {
    private List<OverviewRatingDetailsViewModel> listRatings;

    private double totalQuantityRatings;
    private float totalRatings;

    public List<OverviewRatingDetailsViewModel> getListRatings() {
        if(this.listRatings == null){
            this.listRatings = new ArrayList<>();
        }
        return listRatings;
    }

    public void setListRatings(List<OverviewRatingDetailsViewModel> listRatings) {
        this.listRatings = listRatings;
    }

    public double getTotalQuantityRatings() {
        return totalQuantityRatings;
    }

    public void setTotalQuantityRatings(double totalQuantityRatings) {
        this.totalQuantityRatings = totalQuantityRatings;
    }

    public float getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(float totalRatings) {
        this.totalRatings = totalRatings;
    }
}
