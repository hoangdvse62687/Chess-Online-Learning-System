package com.chess.chessapi.controllers;

import com.chess.chessapi.constants.AppMessage;
import com.chess.chessapi.constants.AppRole;
import com.chess.chessapi.constants.GameHistoryStatus;
import com.chess.chessapi.entities.GameHistory;
import com.chess.chessapi.exceptions.AccessDeniedException;
import com.chess.chessapi.exceptions.BadRequestException;
import com.chess.chessapi.exceptions.ResourceNotFoundException;
import com.chess.chessapi.models.*;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.services.GameHistoryService;
import com.chess.chessapi.services.RedisChessGameService;
import com.chess.chessapi.services.UserService;
import com.chess.chessapi.viewmodels.GameHistoryCreateViewModel;
import com.chess.chessapi.viewmodels.GameHistoryUpdateViewModel;
import com.chess.chessapi.viewmodels.GameHistoryViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@Api(value = "Exercise Management")
public class GameHistoryController {
    @Autowired
    private GameHistoryService gameHistoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisChessGameService redisChessGameService;

    private final String LEARNER_IN_GAME_DONT_HAVE_PERMISSION_CREATE = "Bạn đang trong trận đấu vui lòng hoàn tất trước khi tiếp tục ván đấu khác";

    @ApiOperation(value = "Create game history")
    @PostMapping("/game-history")
    @PreAuthorize("hasAnyAuthority("+ AppRole.ROLE_LEARNER_AUTHENTICATIION+")")
    public @ResponseBody JsonResult createGameHistory(@RequestBody @Valid GameHistoryCreateViewModel gameHistoryCreateViewModel, BindingResult bindingResult){
        String message = "";
        boolean isSuccess = true;
        GameHistoryCreateResponse gameHistoryCreateResponse = new GameHistoryCreateResponse();
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            throw new BadRequestException(message);
        }else{
            try{
                UserPrincipal userPrincipal = this.userService.getCurrentUser();
                if(this.gameHistoryService.isLearnerInGame(userPrincipal.getId())){
                    throw new AccessDeniedException(LEARNER_IN_GAME_DONT_HAVE_PERMISSION_CREATE);
                }
                gameHistoryCreateResponse = this.gameHistoryService.create(gameHistoryCreateViewModel,userPrincipal.getId());
                message = AppMessage.getMessageSuccess(AppMessage.CREATE,AppMessage.GAME_HISTORY);
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.CREATE,AppMessage.GAME_HISTORY);
                isSuccess = false;
                Logger.getLogger(GameHistoryController.class.getName()).log(Level.SEVERE,null,ex);
            }
        }
        gameHistoryCreateResponse.setSuccess(isSuccess);
        return new JsonResult(message,gameHistoryCreateResponse);
    }

    @ApiOperation(value = "Get current user game history paginations")
    @GetMapping("/game-history/current-user")
    @PreAuthorize("hasAnyAuthority("+ AppRole.ROLE_LEARNER_AUTHENTICATIION+")")
    public @ResponseBody JsonResult getGameHistory(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize){
        UserPrincipal userPrincipal = this.userService.getCurrentUser();
        PagedList<GameHistoryViewModel> data = null;
        try {
            data = this.gameHistoryService.getPagination(page,pageSize,userPrincipal.getId());
        }catch (IllegalArgumentException ex){
            Logger.getLogger(GameHistoryController.class.getName()).log(Level.SEVERE,null,ex);
            throw new ResourceNotFoundException("Page","number",page);
        }
        return new JsonResult(null,data);
    }

    @ApiOperation(value = "Get current user game history on redis")
    @GetMapping("/game-history/current-user/redis-data")
    @PreAuthorize("hasAnyAuthority("+ AppRole.ROLE_LEARNER_AUTHENTICATIION+")")
    public @ResponseBody JsonResult getDataOnRedis(){
        UserPrincipal userPrincipal = this.userService.getCurrentUser();
        ChessGame chessGame = null;
        try {
            GameHistory gameHistory = this.gameHistoryService.getLastestGameHistoryByUserId(userPrincipal.getId());
            if(gameHistory != null){
                chessGame = this.redisChessGameService.find(gameHistory.getGamehistoryId());
            }
        }catch (Exception ex){
            Logger.getLogger(GameHistoryController.class.getName()).log(Level.SEVERE,null,ex);
        }
        return new JsonResult(null,chessGame);
    }
}
