package com.chess.chessapi.services;

import com.chess.chessapi.constants.EntitiesFieldName;
import com.chess.chessapi.constants.GameHistoryStatus;
import com.chess.chessapi.constants.KFactor;
import com.chess.chessapi.entities.GameHistory;
import com.chess.chessapi.entities.User;
import com.chess.chessapi.models.GameHistoryCreateResponse;
import com.chess.chessapi.models.PagedList;
import com.chess.chessapi.models.PredictionEloStockfish;
import com.chess.chessapi.repositories.GameHistoryRepository;
import com.chess.chessapi.utils.EloRatingUtils;
import com.chess.chessapi.utils.ManualCastUtils;
import com.chess.chessapi.utils.TimeUtils;
import com.chess.chessapi.viewmodels.GameHistoryCreateViewModel;
import com.chess.chessapi.viewmodels.GameHistoryUpdateViewModel;
import com.chess.chessapi.viewmodels.GameHistoryViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GameHistoryService {
    @Autowired
    private GameHistoryRepository gameHistoryRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PointLogService pointLogService;

    private final String BET_MESSAGE = "Bạn đã đặt cược ";
    private final String DRAWN_MESSAGE = "Bạn đã hòa máy cấp độ ";
    private final String LOSE_MESSAGE = "Bạn đã thua máy cấp độ ";
    private final String WIN_MESSAGE = "Bạn đã thắng máy cấp độ ";
    private final String POINT = " điểm ";
    private final String WHEN_PLAYING_BOT = "khi chơi cờ với máy cấp độ ";
    private final String RECEIVE = ". Và nhận được ";
    private final String LOSSE = ".Và mất ";

    //PUBLIC METHOD DEFINED
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public GameHistoryCreateResponse create(GameHistoryCreateViewModel gameHistoryCreateViewModel, long userId){
        GameHistory gameHistory = new GameHistory();
        gameHistory.setColor(gameHistoryCreateViewModel.getColor());
        gameHistory.setGameTime(gameHistoryCreateViewModel.getGameTime());
        gameHistory.setLevel(gameHistoryCreateViewModel.getLevel());
        gameHistory.setRecord("");
        gameHistory.setStartTime(TimeUtils.getCurrentTime());
        gameHistory.setStatus(GameHistoryStatus.BET);
        User user = new User();
        user.setUserId(userId);
        gameHistory.setUser(user);


        GameHistory savedGameHistory = this.gameHistoryRepository.save(gameHistory);

        GameHistoryCreateResponse gameHistoryCreateResponse = new GameHistoryCreateResponse();
        gameHistoryCreateResponse.setSavedId(savedGameHistory.getGamehistoryId());
        PredictionEloStockfish predictionEloStockfish = new PredictionEloStockfish();
        float userElo = this.userService.getPointByUserId(userId);
        predictionEloStockfish.setPredictionWinningElo(this.getUserEloPointByStatus(userElo,gameHistory.getLevel()
                ,GameHistoryStatus.WIN,gameHistory.getUser().getUserId(),true));
        predictionEloStockfish.setPredictionLoseElo(this.getUserEloPointByStatus(userElo,gameHistory.getLevel()
                ,GameHistoryStatus.LOSE,gameHistory.getUser().getUserId(),true));
        predictionEloStockfish.setPredictionDrawnElo(this.getUserEloPointByStatus(userElo,gameHistory.getLevel()
                ,GameHistoryStatus.DRAWN,gameHistory.getUser().getUserId(),true));
        gameHistoryCreateResponse.setPredictionEloStockfish(predictionEloStockfish);
        return gameHistoryCreateResponse;
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void update(GameHistory gameHistory, long userId){

        this.handleDataCreateUpdate(userId,gameHistory);

        this.gameHistoryRepository.save(gameHistory);
    }

    public PagedList<GameHistoryViewModel> getPagination(int page, int pageSize, long userId){
        PageRequest pageable = PageRequest.of(page - 1,pageSize, Sort.by(EntitiesFieldName.GAME_HISTORY_START_TIME).descending());

        return this.fillDataToPaginationCustom(this.gameHistoryRepository.findAllByUserId(pageable,userId));
    }

    public Optional<GameHistory> getById(long gameHistoryId){
        return this.gameHistoryRepository.findById(gameHistoryId);
    }

    public int getUserEloPointByStatus(float userElo,int level,int status,long userId,boolean isPrediction){
        return EloRatingUtils.getEloByStockfishLevel(userElo
                ,level,status, KFactor.getKFactor(userElo,this.isUnderThirtyGame(userId,isPrediction)));
    }

    public String getContentPointLog(int status,float point,int level) {
        String content = "";
        point = point < 0 ? -point : point;
        String pointStr = Integer.toString((int)point);
        switch (status) {
            case GameHistoryStatus.BET:
                content = BET_MESSAGE + pointStr + POINT + WHEN_PLAYING_BOT + level;
                break;
            case GameHistoryStatus.DRAWN:
                content = DRAWN_MESSAGE + level + RECEIVE + pointStr + POINT;
                break;
            case GameHistoryStatus.LOSE:
                content = LOSE_MESSAGE + level + LOSSE + pointStr + POINT;
                break;
            case GameHistoryStatus.WIN:
                content = WIN_MESSAGE + level + RECEIVE + pointStr + POINT;
                break;

        }
        return content;
    }

    public boolean isLearnerInGame(long userId){
        Integer status = this.gameHistoryRepository.findLastGameHistoryStatusByUserId(userId);
        if(status == null){
            return false;
        }
        if(status == GameHistoryStatus.BET){
            return true;
        }
        return false;
    }

    public GameHistory getLastestGameHistoryByUserId(long userId){
        return this.gameHistoryRepository.findLastGameHistoryByUserId(userId);
    }
    //END PUBLIC METHOD DEFINED

    //PRIVATE METHOD DEFINED
    private void handleDataCreateUpdate(long userId, GameHistory gameHistory){

        this.userService.increasePoint(userId,gameHistory.getPoint());

        //write point log
        String content = this.getContentPointLog(gameHistory.getStatus(),
                gameHistory.getPoint(),gameHistory.getLevel());
        this.pointLogService.create(content,(int)gameHistory.getPoint(),userId);
    }

    private PagedList<GameHistoryViewModel> fillDataToPaginationCustom(Page<Object> rawData){
        final List<GameHistoryViewModel> content = ManualCastUtils.castPageObjectsToGameHistoryViewModel(rawData);
        final int totalPages = rawData.getTotalPages();
        final long totalElements = rawData.getTotalElements();
        return new PagedList<GameHistoryViewModel>(totalPages,totalElements,content);
    }

    private boolean isUnderThirtyGame(long userId,boolean isPrediction){
        int counter = this.gameHistoryRepository.countByUserId(userId);

        return counter > (isPrediction ? 31 : 30) ? false : true;
    }
    //END PRIVATE METHOD DEFINED
}
