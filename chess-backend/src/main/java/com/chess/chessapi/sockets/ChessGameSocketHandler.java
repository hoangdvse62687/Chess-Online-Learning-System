package com.chess.chessapi.sockets;

import com.chess.chessapi.constants.GameHistoryStatus;
import com.chess.chessapi.entities.GameHistory;
import com.chess.chessapi.jobs.ChessGameJobListener;
import com.chess.chessapi.models.*;
import com.chess.chessapi.services.CronJobSchedulerService;
import com.chess.chessapi.services.GameHistoryService;
import com.chess.chessapi.services.RedisChessGameService;
import com.chess.chessapi.services.UserService;
import com.chess.chessapi.utils.TimeUtils;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

public class ChessGameSocketHandler implements WebSocketHandler {

    @Autowired
    private GameHistoryService gameHistoryService;

    @Autowired
    private RedisChessGameService redisChessGameService;

    @Autowired
    private UserService userService;

    private Gson gson = new Gson();

    @Autowired
    private CronJobSchedulerService cronJobSchedulerService;

    public static final int TURN_PLAYER_1 = 1;
    public static final int TURN_PLAYER_2 = 2;

    private final String ON_CONNECT_SUCCESS = "Kết nối thành công";
    private final String ON_CONNECT_FAIL = "Kết nối không thành công";
    private final String INIT_CHESS_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private static final Logger log = LoggerFactory.getLogger(ChessGameSocketHandler.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        boolean isConnectSuccess = true;
        boolean isReconnect = false;
        String message = "";
        ChessGameInitResponse chessGameInitResponse = new ChessGameInitResponse();
        try{
            GameHistory gameHistory = this.gameHistoryService.getById(getGameHistoryIdInSession(session)).get();
            //Only allow game is in process
            if(gameHistory.getStatus() == GameHistoryStatus.BET){
                ChessGame chessGame = this.redisChessGameService.find(gameHistory.getGamehistoryId());
                //if chess game is in redis => chess game is in process,else chess game is start to init
                if(chessGame == null){
                    this.redisChessGameService.save(this.initChessBotGame(gameHistory));
                }else{
                    //delete auto handle trigger game and using websocket handle instead
                    this.cronJobSchedulerService.deleteJobTask(Long.toString(gameHistory.getGamehistoryId()), ChessGameJobListener.LISTENER_NAME);
                    chessGameInitResponse.setChessGame(chessGame);
                    isReconnect = true;
                }
                message = ON_CONNECT_SUCCESS;
            }else{
                throw new Exception();
            }
        }catch (Exception ex){
            isConnectSuccess = false;
            message = ON_CONNECT_FAIL;
            log.info(ex.getMessage());
        }finally {
            chessGameInitResponse.setConnectSuccess(isConnectSuccess);
            chessGameInitResponse.setReconnect(isReconnect);
            JsonResult jsonResult = new JsonResult(message,chessGameInitResponse);
            session.sendMessage(new TextMessage(gson.toJson(jsonResult)));
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        try{
            ChessGame chessGame = this.redisChessGameService.find(getGameHistoryIdInSession(session));
            if(chessGame != null){
                ChessGameMove chessGameMove = gson.fromJson((String)message.getPayload(),ChessGameMove.class);
                //update chess game in redis
                JsonResult jsonResult = updateChessGame(chessGame,chessGameMove);
                if(jsonResult != null){
                    session.sendMessage(new TextMessage(gson.toJson(jsonResult)));
                }
            }
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        try{
            ChessGame chessGame = this.redisChessGameService.find(getGameHistoryIdInSession(session));
            if(chessGame != null){
                //setup next trigger end chess game
                Timestamp taskTimeTrigger = TimeUtils.getCurrentTime();
                long estimateTimeTrigger = 0;
                int statusIfEnd = GameHistoryStatus.BET;
                if(chessGame.getNextTurnPlayer() == TURN_PLAYER_1){
                    chessGame.getPlayer1().setSecondCountDown(chessGame.getPlayer1().getSecondCountDown()
                            - TimeUtils.getDurationSecond(chessGame.getLastAccessed(),TimeUtils.getCurrentTime()));
                    estimateTimeTrigger = chessGame.getPlayer1().getSecondCountDown();
                    statusIfEnd = GameHistoryStatus.LOSE;
                }else{
                    chessGame.getPlayer2().setSecondCountDown(chessGame.getPlayer2().getSecondCountDown()
                            - TimeUtils.getDurationSecond(chessGame.getLastAccessed(),TimeUtils.getCurrentTime()));
                    estimateTimeTrigger = chessGame.getPlayer2().getSecondCountDown();
                    statusIfEnd = GameHistoryStatus.WIN;
                }
                if(estimateTimeTrigger <= 0){
                    updateEloPlayer(chessGame,statusIfEnd);
                }else{
                    chessGame.setLastAccessed(TimeUtils.getCurrentTime());
                    taskTimeTrigger.setTime(chessGame.getLastAccessed().getTime()
                            + TimeUnit.SECONDS.toMillis(estimateTimeTrigger));
                    this.cronJobSchedulerService.createJobTask(TimeUtils.toCron(taskTimeTrigger)
                            ,chessGame.getGameHistoryId().toString(), ChessGameJobListener.LISTENER_NAME);
                    this.redisChessGameService.update(chessGame);
                }
            }
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private Long getGameHistoryIdInSession(WebSocketSession session) throws NumberFormatException{
        String sessionPath = session.getUri().getPath();
        return Long.parseLong(sessionPath.substring(sessionPath.lastIndexOf('/') + 1,sessionPath.length()));
    }

    private ChessGame initChessBotGame(GameHistory gameHistory){
        ChessGame chessGame = new ChessGame();
        chessGame.setColor(gameHistory.getColor());
        chessGame.setGameContent(gameHistory.getRecord());
        chessGame.setGameHistoryId(gameHistory.getGamehistoryId());
        chessGame.setLevel(gameHistory.getLevel());
        chessGame.setSecondGameTime(gameHistory.getGameTime());
        chessGame.setLastAccessed(TimeUtils.getCurrentTime());
        chessGame.setStatus(gameHistory.getStatus());
        PlayerInfo player1 = new PlayerInfo();
        player1.setBot(false);
        player1.setSecondCountDown(gameHistory.getGameTime());
        chessGame.setPlayer1(player1);
        PlayerInfo player2 = new PlayerInfo();
        player2.setBot(true);
        player2.setSecondCountDown(gameHistory.getGameTime());
        chessGame.setPlayer2(player2);

        PredictionEloStockfish predictionEloStockfish = new PredictionEloStockfish();
        float userElo = this.userService.getPointByUserId(gameHistory.getUser().getUserId());
        predictionEloStockfish.setPredictionWinningElo(this.gameHistoryService.getUserEloPointByStatus(userElo,gameHistory.getLevel()
                ,GameHistoryStatus.WIN,gameHistory.getUser().getUserId(),true));
        predictionEloStockfish.setPredictionLoseElo(this.gameHistoryService.getUserEloPointByStatus(userElo,gameHistory.getLevel()
                ,GameHistoryStatus.LOSE,gameHistory.getUser().getUserId(),true));
        predictionEloStockfish.setPredictionDrawnElo(this.gameHistoryService.getUserEloPointByStatus(userElo,gameHistory.getLevel()
                ,GameHistoryStatus.DRAWN,gameHistory.getUser().getUserId(),true));
        chessGame.setPredictionEloStockfish(predictionEloStockfish);
        //player chosen color white, player plays first
        if(gameHistory.getColor() == 0){
            chessGame.setNextTurnPlayer(TURN_PLAYER_1);
        }else{
            chessGame.setNextTurnPlayer(TURN_PLAYER_2);
        }

        chessGame.setCurrentFen(INIT_CHESS_FEN);
        return chessGame;
    }

    private JsonResult updateChessGame(ChessGame chessGame,ChessGameMove chessGameMove)throws ParseException {
        //expect next player move
        if(chessGame.getNextTurnPlayer() != chessGameMove.getTurnPlayer()){
            return null;
        }

        if(chessGameMove.getTurnPlayer() == TURN_PLAYER_1){
            chessGame.getPlayer1().setSecondCountDown(chessGame.getPlayer1().getSecondCountDown() - TimeUtils.getDurationSecond(
                    chessGame.getLastAccessed(),TimeUtils.getCurrentTime()));
            chessGame.setNextTurnPlayer(TURN_PLAYER_2);
        }else if(chessGameMove.getTurnPlayer() == TURN_PLAYER_2){
            chessGame.getPlayer2().setSecondCountDown(chessGame.getPlayer2().getSecondCountDown() - TimeUtils.getDurationSecond(
                    chessGame.getLastAccessed(),TimeUtils.getCurrentTime()));
            chessGame.setNextTurnPlayer(TURN_PLAYER_1);
        }

        chessGame.setGameContent(chessGame.getGameContent().concat(chessGameMove.getMove() + " "));
        chessGame.setLastAccessed(TimeUtils.getCurrentTime());
        chessGame.setCurrentFen(chessGameMove.getCurrentFen());

        //check if game is over or status is end
        boolean isEnd = false;
        if(chessGameMove.getStatus() != GameHistoryStatus.BET){
            isEnd = true;
        }else if(chessGame.getPlayer1().getSecondCountDown() <= 0){
            isEnd = true;
        }else if(chessGame.getPlayer2().getSecondCountDown() <= 0){
            isEnd = true;
        }
        // status is result player 1 win , lose or drawn with player 2
        if(isEnd){
            return updateEloPlayer(chessGame,chessGameMove.getStatus());
        }else{
            this.redisChessGameService.update(chessGame);
        }
        return null;
    }

    private JsonResult updateEloPlayer(ChessGame chessGame,int status){
        GameHistory gameHistory = this.gameHistoryService.getById(chessGame.getGameHistoryId()).get();
        gameHistory.setStatus(status);
        float userElo = this.userService.getPointByUserId(gameHistory.getUser().getUserId());
        gameHistory.setPoint(this.gameHistoryService.getUserEloPointByStatus(userElo,gameHistory.getLevel()
                ,gameHistory.getStatus(),gameHistory.getUser().getUserId(),false));
        gameHistory.setRecord(chessGame.getGameContent());
        this.gameHistoryService.update(gameHistory,gameHistory.getUser().getUserId());
        this.redisChessGameService.deleteById(gameHistory.getGamehistoryId());
        return new JsonResult(this.gameHistoryService.getContentPointLog
                (gameHistory.getStatus(),gameHistory.getPoint(),gameHistory.getLevel()),null);
    }
}
