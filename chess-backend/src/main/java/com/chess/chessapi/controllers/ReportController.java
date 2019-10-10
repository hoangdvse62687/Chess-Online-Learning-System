package com.chess.chessapi.controllers;

import com.chess.chessapi.constants.AppRole;
import com.chess.chessapi.models.JsonResult;
import com.chess.chessapi.services.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/reports")
@Api(value = "Report Management")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @ApiOperation(value = "Get Learner Status Course Report ")
    @GetMapping(value = "/learner-status-course")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION +")")
    public @ResponseBody
    JsonResult getLearnerStatusCourse(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize
	,String courseName,String sortBy,String sortDirection) {
		if(courseName == null){
            courseName = "";
        }
        if(sortDirection == null){
            sortDirection = "";
        }
        if(sortBy == null){
            sortBy = "";
        }
        return new JsonResult("",this.reportService.getLearnerStatusReport(page,pageSize,courseName,sortBy,sortDirection));
    }

    @ApiOperation(value = "Get Enrollment Report")
    @GetMapping(value = "/enrollment-report")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION +")")
    public @ResponseBody
    JsonResult getEnrollment(@RequestParam("year") int year) {
        return new JsonResult("",this.reportService.getEnrollmentReport(year));
    }

    @ApiOperation(value = "Get Users Register Report")
    @GetMapping(value = "/users-register-report")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_ADMIN_AUTHENTICATIION +")")
    public @ResponseBody
    JsonResult getUsersRegister(@RequestParam("year") int year) {
        return new JsonResult("",this.reportService.getUsersRegisterReport(year));
    }

    @ApiOperation(value = "Get Rate Winnable Report")
    @GetMapping(value = "/rate-winnable-report")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_ADMIN_AUTHENTICATIION +")")
    public @ResponseBody
    JsonResult getRateWinnable(@RequestParam("year") int year) {
        return new JsonResult("",this.reportService.getRateWinnableReport(year));
    }

    @ApiOperation(value = "Get Rate Winnable Level Report")
    @GetMapping(value = "/rate-winnable-level-report")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_ADMIN_AUTHENTICATIION +")")
    public @ResponseBody
    JsonResult getRateWinnableLevel(@RequestParam("year") int year) {
        return new JsonResult("",this.reportService.getRateWinnableLevelReport(year));
    }

    @ApiOperation(value = "Get Publish Course Report")
    @GetMapping(value = "/publish-course-report")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_ADMIN_AUTHENTICATIION +")")
    public @ResponseBody
    JsonResult getPublishCourseReport() {
        return new JsonResult("",this.reportService.getPublishCourseReport());
    }

    @ApiOperation(value = "Get Course Status Report")
    @GetMapping(value = "/course-status-report")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_ADMIN_AUTHENTICATIION +")")
    public @ResponseBody
    JsonResult getCourseStatusReport() {
        return new JsonResult("",this.reportService.getCourseStatusReport());
    }
}
