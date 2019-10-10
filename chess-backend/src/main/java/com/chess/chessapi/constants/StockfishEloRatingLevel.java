package com.chess.chessapi.constants;

public class StockfishEloRatingLevel {
    public static final int STOCKFISH_LEVEL_1 = 1;
    public static final int STOCKFISH_LEVEL_2 = 2;
    public static final int STOCKFISH_LEVEL_3 = 3;
    public static final int STOCKFISH_LEVEL_4 = 4;
    public static final int STOCKFISH_LEVEL_5 = 5;

    public static final int STOCKFISH_LEVEL_1_ELO = 1302;
    public static final int STOCKFISH_LEVEL_2_ELO = 1427;
    public static final int STOCKFISH_LEVEL_3_ELO = 1589;
    public static final int STOCKFISH_LEVEL_4_ELO = 1722;
    public static final int STOCKFISH_LEVEL_5_ELO = 1862;

    public static int getElo(int StockfishLevel){
        int elo = 0;
        switch (StockfishLevel){
            case STOCKFISH_LEVEL_1:
                elo = STOCKFISH_LEVEL_1_ELO;
                break;
            case STOCKFISH_LEVEL_2:
                elo = STOCKFISH_LEVEL_2_ELO;
                break;
            case STOCKFISH_LEVEL_3:
                elo = STOCKFISH_LEVEL_3_ELO;
                break;
            case STOCKFISH_LEVEL_4:
                elo = STOCKFISH_LEVEL_4_ELO;
                break;
            case STOCKFISH_LEVEL_5:
                elo = STOCKFISH_LEVEL_5_ELO;
                break;
        }
        return elo;
    }
}
