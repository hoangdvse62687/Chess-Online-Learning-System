package com.chess.chessapi.controllers;

import com.chess.chessapi.constants.AppMessage;
import com.chess.chessapi.constants.AppRole;
import com.chess.chessapi.exceptions.BadRequestException;
import com.chess.chessapi.exceptions.ResourceNotFoundException;
import com.chess.chessapi.models.JsonResult;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.services.NotificationService;
import com.chess.chessapi.services.UserService;
import com.chess.chessapi.viewmodels.NotificationPaginationsViewModel;
import com.chess.chessapi.viewmodels.NotificationTokenUpdateViewModel;
import com.chess.chessapi.viewmodels.UpdateIsViewedNotification;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/notifications")
@Api(value = "Notification Management")
public class NotificationController {
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;

    @ApiOperation(value = "Get current user notification pagings")
    @GetMapping("/current-user")
    @PreAuthorize("isAuthenticated()")
    public JsonResult getNotifications(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize,
                                       String sortBy,String sortDirection){
        if(sortBy == null){
            sortBy = "";
        }
        if(sortDirection == null){
            sortDirection = "";
        }
        UserPrincipal currentUser = this.userService.getCurrentUser();

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.addAll(currentUser.getAuthorities());
        long role_id = AppRole.ROLE_LEARNER;
        for (GrantedAuthority authority:
             authorities) {
            role_id = Integer.parseInt(authority.toString());
        }
        NotificationPaginationsViewModel data = null;
        try{
            data = this.notificationService
                    .getPagination(page,pageSize, role_id,currentUser.getId(),sortBy,sortDirection);
        }catch (IllegalArgumentException ex){
            Logger.getLogger(NotificationController.class.getName()).log(Level.SEVERE,null,ex);
            throw new ResourceNotFoundException("Page","number",page);
        }
        return new JsonResult(null,data);
    }

    @ApiOperation(value = "update is view by list notification ids")
    @PutMapping("/is-viewed")
    @PreAuthorize("isAuthenticated()")
    public JsonResult updateIsViewed(@RequestBody @Valid UpdateIsViewedNotification updateIsViewedNotification, BindingResult bindingResult){
        String message = "";
        boolean isSuccess = true;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            throw new BadRequestException(message);
        }else {
            try{
                this.notificationService.updateIsView(updateIsViewedNotification.getNotificationIds());
                message = AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.NOTIFICATION);
            }catch (DataIntegrityViolationException ex){
                isSuccess = false;
                message = AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.NOTIFICATION);
                Logger.getLogger(NotificationController.class.getName()).log(Level.SEVERE,null,ex);
            }
        }

        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "update notification token")
    @PostMapping("/token")
    @PreAuthorize("isAuthenticated()")
    public JsonResult updateNotificationToken(@RequestBody NotificationTokenUpdateViewModel notificationTokenUpdateViewModel){
        UserPrincipal userPrincipal = this.userService.getCurrentUser();

        this.notificationService.updateNotificationTokenId(userPrincipal.getId(),userPrincipal.getRole(),notificationTokenUpdateViewModel.getToken());
        return new JsonResult("",true);
    }
}
