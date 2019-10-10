package com.chess.chessapi.jobs;

import com.chess.chessapi.constants.GameHistoryStatus;
import com.chess.chessapi.entities.GameHistory;
import com.chess.chessapi.models.ChessGame;
import com.chess.chessapi.services.GameHistoryService;
import com.chess.chessapi.services.RedisChessGameService;
import com.chess.chessapi.services.UserService;
import com.chess.chessapi.sockets.ChessGameSocketHandler;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChessGameJob implements Job {

    @Autowired
    private GameHistoryService gameHistoryService;

    @Autowired
    private RedisChessGameService redisChessGameService;

    @Autowired
    private UserService userService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try{
            ChessGame chessGame = this.redisChessGameService.find(Long.parseLong(context.getJobDetail().getKey().getName()));
            if(chessGame != null){
                //because trigger is fired => next player turn is the lose
                //player 1 is the player and player2 is the bot
                GameHistory gameHistory = this.gameHistoryService.getById(chessGame.getGameHistoryId()).get();
                if(gameHistory.getStatus() == GameHistoryStatus.BET){
                    float userElo = this.userService.getPointByUserId(gameHistory.getUser().getUserId());
                    if(chessGame.getNextTurnPlayer() == ChessGameSocketHandler.TURN_PLAYER_1){
                        gameHistory.setStatus(GameHistoryStatus.LOSE);
                    }else{
                        gameHistory.setStatus(GameHistoryStatus.WIN);
                    }
                    gameHistory.setPoint(this.gameHistoryService.getUserEloPointByStatus(userElo,gameHistory.getLevel()
                            ,gameHistory.getStatus(),gameHistory.getUser().getUserId(),false));
                    this.gameHistoryService.update(gameHistory,gameHistory.getUser().getUserId());
                    this.redisChessGameService.deleteById(gameHistory.getGamehistoryId());
                }
            }
        }catch (NumberFormatException ex){

        }
    }
}
