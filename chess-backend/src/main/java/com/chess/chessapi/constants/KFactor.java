package com.chess.chessapi.constants;

public class KFactor {
//    Currently, the USCF uses a formula which calculates the K-factor based on factors including the number of games played and the player's rating. The K-factor is also reduced for high rated players if the event has shorter time controls. [19]
//    FIDE uses the following ranges
    public static final int NEW_STARTER_THIRTY_GAME = 40;
    public static final int UNDER_2400_ELO = 20;
    public static final int ABOVE_2400_ELO = 10;

    public static int getKFactor(float userElo,boolean isUnderThirtyGame){
        if(isUnderThirtyGame){
            return NEW_STARTER_THIRTY_GAME;
        }else if(userElo < 2400){
            return UNDER_2400_ELO;
        }else {
            return ABOVE_2400_ELO;
        }
    }
}
