package com.chess.chessapi.models;

import java.util.ArrayList;
import java.util.List;

public class SuggestionAlgorithmData {
    private List<UserSuggestionAlgorithm> allUser;

    private List<Long> allCourse;

    public List<UserSuggestionAlgorithm> getAllUser() {
        if(this.allUser == null){
            this.allUser = new ArrayList<>();
        }
        return allUser;
    }

    public void setAllUser(List<UserSuggestionAlgorithm> allUser) {
        this.allUser = allUser;
    }

    public List<Long> getAllCourse() {
        if(this.allCourse == null){
            this.allCourse = new ArrayList<>();
        }
        return allCourse;
    }

    public void setAllCourse(List<Long> allCourse) {
        this.allCourse = allCourse;
    }
}
