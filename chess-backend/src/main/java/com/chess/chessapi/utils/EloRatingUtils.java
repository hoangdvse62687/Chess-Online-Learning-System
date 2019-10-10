package com.chess.chessapi.utils;

import com.chess.chessapi.constants.GameHistoryStatus;
import com.chess.chessapi.constants.StockfishEloRatingLevel;

import java.io.Serializable;

public class EloRatingUtils implements Serializable {

    private static float probability(float rating1,
                             float rating2)
    {
        return 1.0f * 1.0f / (1 + 1.0f *
                (float)(Math.pow(10, 1.0f *
                        (rating1 - rating2) / 400)));
    }

    private static float eloRating(float Ra, float Rb,
                          int K, int d)
    {
        float Pa = probability(Rb, Ra);
        float result;
        if (d == GameHistoryStatus.WIN) {
            //win
            result = K * (1 - Pa);
        }
        else if(d == GameHistoryStatus.LOSE){
            //lose
            result = K * (0 - Pa);
        }else{
            //drawn
            result = K * (0.5f - Pa);
        }
        return result;
    }

    public static int getEloByStockfishLevel(float userElo,int stockfishLevel,int status,int K){
        float stockfishElo = StockfishEloRatingLevel.getElo(stockfishLevel);
        switch (status){
            case GameHistoryStatus.WIN:
                return (int)eloRating(userElo,stockfishElo,K,GameHistoryStatus.WIN);
            case GameHistoryStatus.LOSE:
                return (int)eloRating(userElo,stockfishElo,K,GameHistoryStatus.LOSE);
            default:
                return (int)eloRating(userElo,stockfishElo,K,GameHistoryStatus.DRAWN);
        }
    }
}
