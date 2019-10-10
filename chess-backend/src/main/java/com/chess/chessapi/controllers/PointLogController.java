package com.chess.chessapi.controllers;

import com.chess.chessapi.constants.AppRole;
import com.chess.chessapi.exceptions.ResourceNotFoundException;
import com.chess.chessapi.models.JsonResult;
import com.chess.chessapi.models.PagedList;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.services.PointLogService;
import com.chess.chessapi.services.UserService;
import com.chess.chessapi.viewmodels.PointLogViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/point-logs")
@Api(value = "Point Log Management")
public class PointLogController {
    @Autowired
    private PointLogService pointLogService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Get current user point log pagination")
        @GetMapping("/current-user")
    @PreAuthorize("hasAnyAuthority("+ AppRole.ROLE_LEARNER_AUTHENTICATIION+")")
    public @ResponseBody
    JsonResult getCurrentUserPointLogPagination(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize
            ,String sortBy, String sortDirection){
        if(sortBy == null){
            sortBy = "";
        }
        if(sortDirection == null){
            sortDirection = "";
        }
        PagedList<PointLogViewModel> data = null;
        try{
            UserPrincipal userPrincipal = this.userService.getCurrentUser();
            data = this.pointLogService.getPointLogPaginationByUserId(userPrincipal.getId(),page,pageSize,sortBy,sortDirection);
        }catch (IllegalArgumentException ex){
            Logger.getLogger(PointLogController.class.getName()).log(Level.SEVERE,null,ex);
            throw new ResourceNotFoundException("Page","number",page);
        }

        return new JsonResult(null,data);
    }
}
