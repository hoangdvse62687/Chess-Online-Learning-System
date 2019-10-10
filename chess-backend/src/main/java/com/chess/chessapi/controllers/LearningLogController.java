package com.chess.chessapi.controllers;

import com.chess.chessapi.constants.AppMessage;
import com.chess.chessapi.constants.AppRole;
import com.chess.chessapi.entities.LearningLog;
import com.chess.chessapi.exceptions.AccessDeniedException;
import com.chess.chessapi.exceptions.BadRequestException;
import com.chess.chessapi.exceptions.ResourceNotFoundException;
import com.chess.chessapi.models.CreateResponse;
import com.chess.chessapi.models.JsonResult;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.services.LearningLogService;
import com.chess.chessapi.services.UserService;
import com.chess.chessapi.viewmodels.LearningLogCreateResponse;
import com.chess.chessapi.viewmodels.LearningLogCreateViewModel;
import com.chess.chessapi.viewmodels.LearningLogUpdateResponse;
import com.chess.chessapi.viewmodels.LearningLogUpdateViewModel;
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
@Api(value = "Learning Log Management")
public class LearningLogController {
    @Autowired
    private LearningLogService learningLogService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Create learning log")
    @PostMapping(value = "/learning-logs")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_LEARNER_AUTHENTICATIION+")")
    public @ResponseBody JsonResult createLearningLog(@Valid @RequestBody LearningLogCreateViewModel learningLogCreateViewModel, BindingResult bindingResult){
        String message = "";
        boolean isSuccess = true;
        LearningLogCreateResponse learningLogCreateResponse = new LearningLogCreateResponse();
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            throw new BadRequestException(message);
        }else{
            try{
                UserPrincipal userPrincipal = this.userService.getCurrentUser();
                if(this.learningLogService.isExist(userPrincipal.getId()
                        ,learningLogCreateViewModel.getCourseId(),learningLogCreateViewModel.getLessonId())){
                    throw new BadRequestException(AppMessage.PERMISSION_DENY_MESSAGE);
                }
                learningLogCreateResponse = this.learningLogService.create(learningLogCreateViewModel,userPrincipal.getId());
                message =  AppMessage.getMessageSuccess(AppMessage.CREATE,AppMessage.LEARNING_LOG);
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.CREATE,AppMessage.LEARNING_LOG);
                isSuccess = false;
                Logger.getLogger(LearningLogController.class.getName()).log(Level.SEVERE,null,ex);
            }
        }
        learningLogCreateResponse.setSuccess(isSuccess);
        return new JsonResult(message,learningLogCreateResponse);
    }

    @ApiOperation(value = "Update learning log")
    @PutMapping(value = "/learning-logs")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_LEARNER_AUTHENTICATIION+")")
    public @ResponseBody JsonResult updateLearningLog(@Valid @RequestBody LearningLogUpdateViewModel learningLogUpdateViewModel, BindingResult bindingResult){
        String message = "";
        boolean isSuccess = true;
        LearningLogUpdateResponse learningLogUpdateResponse = new LearningLogUpdateResponse();
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            throw new BadRequestException(message);
        }else{
            try{
                LearningLog learningLog = this.learningLogService.getById(learningLogUpdateViewModel.getLearningLogId())
                        .orElseThrow(() -> new ResourceNotFoundException("Learning log","id",learningLogUpdateViewModel.getLearningLogId()));
                if(learningLog.getUser().getUserId() != this.userService.getCurrentUser().getId()){
                    throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
                }
                learningLogUpdateResponse.setComplete(this.learningLogService
                        .update(learningLog,learningLogUpdateViewModel.isPassed()));
                message =  AppMessage.getMessageSuccess(AppMessage.CREATE,AppMessage.LEARNING_LOG);
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.CREATE,AppMessage.LEARNING_LOG);
                isSuccess = false;
                Logger.getLogger(LearningLogController.class.getName()).log(Level.SEVERE,null,ex);
            }
        }
        learningLogUpdateResponse.setSuccess(isSuccess);
        return new JsonResult(message,learningLogUpdateResponse);
    }

    @ApiOperation(value = "get learning log of current user by courseId")
    @GetMapping("/learning-logs/current-user/{courseId}")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_LEARNER_AUTHENTICATIION+")")
    public @ResponseBody JsonResult getLearningLogByCourseId(@PathVariable("courseId") long courseId){
        UserPrincipal userPrincipal = this.userService.getCurrentUser();
        return new JsonResult(null,this.learningLogService.getAllByCourseId(courseId,userPrincipal.getId()));
    }
}
