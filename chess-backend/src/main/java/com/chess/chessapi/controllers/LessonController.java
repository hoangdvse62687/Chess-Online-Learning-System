package com.chess.chessapi.controllers;

import com.chess.chessapi.constants.AppMessage;
import com.chess.chessapi.constants.AppRole;
import com.chess.chessapi.entities.Lesson;
import com.chess.chessapi.exceptions.AccessDeniedException;
import com.chess.chessapi.exceptions.BadRequestException;
import com.chess.chessapi.exceptions.ResourceNotFoundException;
import com.chess.chessapi.models.CreateResponse;
import com.chess.chessapi.models.JsonResult;
import com.chess.chessapi.models.PagedList;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.services.CourseHasLessonService;
import com.chess.chessapi.services.CourseService;
import com.chess.chessapi.services.LessonService;
import com.chess.chessapi.services.UserService;
import com.chess.chessapi.viewmodels.*;
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
@RequestMapping(value = "/lessons")
@Api(value = "Lesson Management")
public class LessonController {
    @Autowired
    private LessonService lessonService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseHasLessonService courseHasLessonService;

    @ApiOperation(value = "get lesson detail by id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority("+ AppRole.ROLE_LEARNER_AUTHENTICATIION+","
            + AppRole.ROLE_ADMIN_AUTHENTICATIION+","
            + AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult getLessonDetailById(@PathVariable("id") long lessonId){
        UserPrincipal userPrincipal = this.userService.getCurrentUser();
        if(!this.lessonService.checkPermissionViewLesson(userPrincipal,lessonId)){
            throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
        }
        Lesson lesson = this.lessonService.getById(lessonId);
        return new JsonResult("",lesson);
    }

    @ApiOperation(value = "Create interactive lesson")
    @PostMapping(value = "/interactive-lesson")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult createInteractiveLesson(@Valid @RequestBody InteractiveLessonCreateViewModel lessonViewModel, BindingResult bindingResult){
        String message = "";
        boolean isSuccess = true;
        long savedId = 0;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            throw new BadRequestException(message);
        }else{
            try{
                //if courseId are default => check permission create lesson on course
                if(lessonViewModel.getCourseId() != 0){
                    if(!this.courseService.checkPermissionModifyCourse(lessonViewModel.getCourseId())){
                        throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
                    }
                }

                UserPrincipal userPrincipal = this.userService.getCurrentUser();
                savedId = this.lessonService.createInteractiveLesson(lessonViewModel,userPrincipal.getId());
                message =  AppMessage.getMessageSuccess(AppMessage.CREATE,AppMessage.INTERACTIVE_LESSON);
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.CREATE,AppMessage.INTERACTIVE_LESSON);
                isSuccess = false;
                Logger.getLogger(LessonController.class.getName()).log(Level.SEVERE,null,ex);
            }
        }
        CreateResponse createResponse = new CreateResponse();
        createResponse.setSuccess(isSuccess);
        createResponse.setSavedId(savedId);
        return new JsonResult(message,createResponse);
    }

    @ApiOperation(value = "Create exercise lesson")
    @PostMapping(value = "/exercise-lesson")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult createExerciseLesson(@Valid @RequestBody ExerciseLessonCreateViewModel lessonViewModel, BindingResult bindingResult){
        String message = "";
        boolean isSuccess = true;
        long savedId = 0;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            throw new BadRequestException(message);
        }else{
            try{
                //if courseId are default => check permission create lesson on course
                if(lessonViewModel.getCourseId() != 0){
                    if(!this.courseService.checkPermissionModifyCourse(lessonViewModel.getCourseId())){
                        throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
                    }
                }

                UserPrincipal userPrincipal = this.userService.getCurrentUser();
                savedId = this.lessonService.createExerciseLesson(lessonViewModel,userPrincipal.getId());
                message =  AppMessage.getMessageSuccess(AppMessage.CREATE,AppMessage.EXERCISE);
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.CREATE,AppMessage.EXERCISE);
                isSuccess = false;
                Logger.getLogger(LessonController.class.getName()).log(Level.SEVERE,null,ex);
            }
        }
        CreateResponse createResponse = new CreateResponse();
        createResponse.setSuccess(isSuccess);
        createResponse.setSavedId(savedId);
        return new JsonResult(message,createResponse);
    }

    @ApiOperation(value = "Update interactive lesson")
    @PutMapping(value = "/interactive-lesson")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult updateInteractiveLesson(@Valid @RequestBody InteractiveLessonUpdateViewModel interactiveLessonUpdateViewModel, BindingResult bindingResult){
        String message = "";
        boolean isSuccess = true;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            throw new BadRequestException(message);
        }else{
            try{
                if(!this.lessonService.checkPermissionModifyLesson(interactiveLessonUpdateViewModel.getLessonId())){
                    throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
                }


                if(!this.lessonService.isExist(interactiveLessonUpdateViewModel.getLessonId())){
                    new ResourceNotFoundException("Lesson","id",interactiveLessonUpdateViewModel.getLessonId());
                }

                this.lessonService.updateInteractiveLesson(interactiveLessonUpdateViewModel);
                message =  AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.INTERACTIVE_LESSON);
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.INTERACTIVE_LESSON);
                isSuccess = false;
                Logger.getLogger(LessonController.class.getName()).log(Level.SEVERE,null,ex);
            }
        }
        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "Update exercise lesson")
    @PutMapping(value = "/exercise-lesson")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult updateExerciseLesson(@Valid @RequestBody ExerciseLessonUpdateViewModel exerciseLessonUpdateViewModel
            , BindingResult bindingResult){

        String message = "";
        boolean isSuccess = true;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            throw new BadRequestException(message);
        }else{
            try{
                if(!this.lessonService.checkPermissionModifyLesson(exerciseLessonUpdateViewModel.getLessonId())){
                    throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
                }


                if(!this.lessonService.isExist(exerciseLessonUpdateViewModel.getLessonId())){
                    new ResourceNotFoundException("Lesson","id",exerciseLessonUpdateViewModel.getLessonId());
                }

                this.lessonService.updateExerciseLesson(exerciseLessonUpdateViewModel);
                message =  AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.EXERCISE);
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.EXERCISE);
                isSuccess = false;
                Logger.getLogger(LessonController.class.getName()).log(Level.SEVERE,null,ex);
            }
        }
        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "Create uninteractive lesson")
    @PostMapping(value = "/uninteractive-lesson")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult createUninteractiveLesson(@Valid @RequestBody UninteractiveLessonCreateViewModel lessonViewModel, BindingResult bindingResult){
        String message = "";
        boolean isSuccess = true;
        long savedId = 0;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            throw new BadRequestException(message);
        }else{
            try{
                //if courseId are default => check permission create lesson on course
                if(lessonViewModel.getCourseId() != 0){
                    if(!this.courseService.checkPermissionModifyCourse(lessonViewModel.getCourseId())){
                        throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
                    }
                }

                UserPrincipal userPrincipal = this.userService.getCurrentUser();
                savedId = this.lessonService.createUninteractiveLesson(lessonViewModel,userPrincipal.getId());
                message =  AppMessage.getMessageSuccess(AppMessage.CREATE,AppMessage.UNINTERACTIVE_LESSON);
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.CREATE,AppMessage.UNINTERACTIVE_LESSON);
                isSuccess = false;
                Logger.getLogger(LessonController.class.getName()).log(Level.SEVERE,null,ex);
            }
        }
        CreateResponse createResponse = new CreateResponse();
        createResponse.setSavedId(savedId);
        createResponse.setSuccess(isSuccess);
        return new JsonResult(message,createResponse);
    }

    @ApiOperation(value = "Update uninteractive lesson")
    @PutMapping(value = "/uninteractive-lesson")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult updateUninteractiveLesson(@Valid @RequestBody UninteractiveLessonUpdateViewModel uninteractiveLessonUpdateViewModel, BindingResult bindingResult){
        String message = "";
        boolean isSuccess = true;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            throw new BadRequestException(message);
        }else{
            try{
                if(!this.lessonService.checkPermissionModifyLesson(uninteractiveLessonUpdateViewModel.getLessonId())){
                    throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
                }

                if(!this.lessonService.isExist(uninteractiveLessonUpdateViewModel.getLessonId())){
                    new ResourceNotFoundException("Lesson","id",uninteractiveLessonUpdateViewModel.getLessonId());
                }

                this.lessonService.updateUninteractiveLesson(uninteractiveLessonUpdateViewModel);
                message =  AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.UNINTERACTIVE_LESSON);
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.UNINTERACTIVE_LESSON);
                isSuccess = false;
                Logger.getLogger(LessonController.class.getName()).log(Level.SEVERE,null,ex);
            }
        }
        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "get lesson of current user")
    @GetMapping("/current-user")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult getLessonByOwner(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize
            ,String name,String sortBy,String sortDirection){
        if(name == null){
            name = "";
        }
        if(sortBy == null){
            sortBy = "";
        }
        if(sortDirection == null) {
            sortDirection = "";
        }

        PagedList<LessonViewModel> data = null;
        UserPrincipal userPrincipal = this.userService.getCurrentUser();
        try{
            data = this.lessonService.getAllByOwner(page,pageSize,name,userPrincipal.getId(),sortBy,sortDirection);
        }catch (IllegalArgumentException ex){
            Logger.getLogger(LessonController.class.getName()).log(Level.SEVERE,null,ex);
            throw new ResourceNotFoundException("Page","number",page);
        }
        return new JsonResult("",data);
    }

    @ApiOperation(value = "Remove lesson from course")
    @DeleteMapping(value = "/lesson-course")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult removeLessonFromCourse(@RequestBody LessonCourseRemoveViewModel lessonCourseRemoveViewModel){
        Boolean isSuccess = true;
        String message = "";
        try{
            if(!this.courseService.checkPermissionModifyCourse(lessonCourseRemoveViewModel.getCourseId())){
                throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
            }

            this.courseHasLessonService.removeLessonFromCourse(lessonCourseRemoveViewModel.getLessonId(),lessonCourseRemoveViewModel.getCourseId());
            message = AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.LESSON);
        }catch (DataIntegrityViolationException ex){
            isSuccess = false;
            message =  AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.LESSON);
            Logger.getLogger(LessonController.class.getName()).log(Level.SEVERE,null,ex);
        }
        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "Remove lesson")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult removeLesson(@PathVariable("id") long lessonId){
        if(!this.lessonService.checkPermissionModifyLesson(lessonId)){
            throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
        }

        Lesson lesson = this.lessonService.getById(lessonId);

        if(lesson == null){
            throw new ResourceNotFoundException("Lesson","id",lessonId);
        }

        Boolean isSuccess = true;
        String message = "";
        try{
            this.lessonService.removeLesson(lesson);
            message = AppMessage.getMessageSuccess(AppMessage.DELETE,AppMessage.LESSON);
        }catch (DataIntegrityViolationException ex){
            isSuccess = false;
            message =  AppMessage.getMessageFail(AppMessage.DELETE,AppMessage.LESSON);
            Logger.getLogger(LessonController.class.getName()).log(Level.SEVERE,null,ex);
        }
        return new JsonResult(message,isSuccess);
    }
    @ApiOperation(value = "mapping lesson to course")
    @PostMapping(value = "/lesson-course")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult mappingLessonCourse(@RequestBody MappingLessonCourseViewModel mappingLessonCourseViewModel){
        if(!this.courseService.checkPermissionModifyCourse(mappingLessonCourseViewModel.getCourseId())){
            throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
        }

        if(!this.lessonService.checkPermissionModifyLesson(mappingLessonCourseViewModel.getLessonId())){
            throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
        }

        if(!this.courseService.isExist(mappingLessonCourseViewModel.getCourseId())){
            new ResourceNotFoundException("Course","id",mappingLessonCourseViewModel.getCourseId());
        }

        Lesson lesson = this.lessonService.getById(mappingLessonCourseViewModel.getLessonId());

        if(lesson == null){
            throw new ResourceNotFoundException("Lesson","id",mappingLessonCourseViewModel.getLessonId());
        }

        Boolean isSuccess = true;
        String message = "";
        try{
            this.lessonService.mappingLessonCourse(mappingLessonCourseViewModel.getLessonId(),lesson.getName()
                    ,mappingLessonCourseViewModel.getCourseId());
            message = AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.LESSON);
        }catch (DataIntegrityViolationException ex){
            isSuccess = false;
            message =  AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.LESSON);
            Logger.getLogger(LessonController.class.getName()).log(Level.SEVERE,null,ex);
        }
        return new JsonResult(message,isSuccess);
    }
}
