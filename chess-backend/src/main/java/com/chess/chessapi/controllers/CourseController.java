package com.chess.chessapi.controllers;

import com.chess.chessapi.constants.AppMessage;
import com.chess.chessapi.constants.AppRole;
import com.chess.chessapi.constants.EloRatingLevel;
import com.chess.chessapi.constants.Status;
import com.chess.chessapi.entities.*;
import com.chess.chessapi.exceptions.NotAcceptedDeniedException;
import com.chess.chessapi.exceptions.AccessDeniedException;
import com.chess.chessapi.exceptions.BadRequestException;
import com.chess.chessapi.exceptions.ResourceNotFoundException;
import com.chess.chessapi.models.CreateResponse;
import com.chess.chessapi.models.JsonResult;
import com.chess.chessapi.models.PagedList;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.services.*;
import com.chess.chessapi.utils.ManualCastUtils;
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@Api(value = "Course Management")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private LearningLogService learningLogService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserHasCourseService userHasCourseService;

    @Autowired
    private SuggestionAlgorithmService suggestionAlgorithmService;

    @ApiOperation(value = "Create course")
    @PostMapping("/courses")
    @PreAuthorize("hasAnyAuthority("+ AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult createCourse(@Valid @RequestBody CourseCreateViewModel course, BindingResult bindingResult){
        String message = "";
        boolean isSuccess = true;
        long savedId = 0;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            throw new BadRequestException(message);
        }else{
            UserPrincipal userPrincipal = this.userService.getCurrentUser();
            try{
                Course savedCourse = this.courseService.create(course,userPrincipal.getId());
                savedId = savedCourse.getCourseId();
                message =  AppMessage.getMessageSuccess(AppMessage.CREATE,AppMessage.COURSE);
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.CREATE,AppMessage.COURSE);
                isSuccess = false;
                Logger.getLogger(CourseController.class.getName()).log(Level.SEVERE,null,ex);
            }
        }
        CreateResponse createResponse = new CreateResponse();
        createResponse.setSavedId(savedId);
        createResponse.setSuccess(isSuccess);
        return new JsonResult(message,createResponse);
    }

    @ApiOperation(value = "Get course pagination")
    @GetMapping("/courses")
    public @ResponseBody JsonResult getCoursePaginationByStatusId(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize
            ,String statusId,String nameCourse,String sortBy,String sortDirection){
        if(nameCourse == null){
            nameCourse = "";
        }
        if(statusId == null){
            statusId = "";
        }
        if(sortBy == null){
            sortBy = "";
        }
        if(sortDirection == null){
            sortDirection = "";
        }
        PagedList<CoursePaginationViewModel> data = null;
        try{
            UserPrincipal userPrincipal = this.userService.getCurrentUser();
            long userId = 0;
            if(userPrincipal != null){
                userId = userPrincipal.getId();
            }
            data = this.courseService.getCoursePaginationByStatusId(nameCourse,page,pageSize, statusId,userId,sortBy,sortDirection);
        }catch (IllegalArgumentException ex){
            Logger.getLogger(CourseController.class.getName()).log(Level.SEVERE,null,ex);
            throw new ResourceNotFoundException("Page","number",page);
        }

        return new JsonResult(null,data);
    }

    @ApiOperation(value = "remove course")
    @PutMapping("/courses/status-to-drafting")
    @PreAuthorize("hasAuthority("+AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult changeStatusToDrafting(@Valid @RequestBody CourseRemoveViewModel courseRemoveViewModel
            , BindingResult bindingResult){

        String message = "";
        Boolean isSuccess = true;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            throw new BadRequestException(message);
        }else {
            try{
                boolean hasPermissionModify = this.courseService.checkPermissionUpdateStatusCourse(courseRemoveViewModel.getCourseId());

                if(hasPermissionModify){
                    if(this.courseService.isExist(courseRemoveViewModel.getCourseId())){
                        this.courseService.updateStatus(courseRemoveViewModel.getCourseId(),Status.COURSE_STATUS_DRAFTED);
                        message = AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.COURSE);
                    }else{
                        throw new ResourceNotFoundException("Course","id",courseRemoveViewModel.getCourseId());
                    }
                }else {
                    throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
                }
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.COURSE);
                isSuccess = false;
                Logger.getLogger(CourseController.class.getName()).log(Level.SEVERE,null,ex);
            }
        }

        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "update course status")
    @PutMapping("/courses/status")
    @PreAuthorize("hasAuthority("+AppRole.ROLE_ADMIN_AUTHENTICATIION+")")
    public @ResponseBody JsonResult updateCourseStatus(@Valid @RequestBody CourseUpdateStatusViewModel courseUpdateStatusViewModel
            , BindingResult bindingResult){
        String message = "";
        Boolean isSuccess = true;

        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            throw new BadRequestException(message);
        }else {
            try{
                //Get course modify
                Course course = this.courseService.getCourseById(courseUpdateStatusViewModel.getCourseId())
                        .orElseThrow(() -> new ResourceNotFoundException("Course","id",courseUpdateStatusViewModel.getCourseId()));
                User user = course.getUser();
                this.courseService.updateCourseStatusByAdmin(course,courseUpdateStatusViewModel,user);
                message = AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.COURSE);
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.COURSE);
                isSuccess = false;
                Logger.getLogger(CourseController.class.getName()).log(Level.SEVERE,null,ex);
            }
        }

        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "get course detail by course id")
    @GetMapping("/courses/{id}")
    public @ResponseBody JsonResult getCourseDetailByCourseId(@PathVariable("id") long courseId){
        Course course = this.courseService.getCourseById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course","id",courseId));
        //get detail
        List<UserDetailViewModel> userDetailViewModels = this.userService.getUserDetailsByCourseId(course.getCourseId());
        course.setUserEnrolleds(this.userService.getUserEnrolls(userDetailViewModels));
        course.setTutors(this.userService.getTutors(userDetailViewModels));
        course.setListCategorys(this.categoryService.getListCategoryIdsByCourseId(course.getCourseId()));
        course.setLessonViewModels(this.lessonService.getLessonByCourseId(course.getCourseId()));
        //check permission to get lesson and learning log
        UserPrincipal userPrincipal = this.userService.getCurrentUser();
        boolean isEnrolled = this.courseService.checkPermissionViewCourseDetail(courseId,userPrincipal);
        boolean isCommented = false;
        if(isEnrolled){
            course.setListLearningLogLessonIds(this.learningLogService.getAllByCourseId(course.getCourseId(),userPrincipal.getId()));
            isCommented = this.reviewService.checkIsComment(userPrincipal.getId(),courseId);
        }
        CourseDetailsViewModel courseDetailsViewModel = ManualCastUtils.castCourseToCourseDetailsViewModel(course,course.getLessonViewModels().size());
        courseDetailsViewModel.setEnrolled(isEnrolled);
        courseDetailsViewModel.setCommented(isCommented);
        return new JsonResult("", courseDetailsViewModel);
    }

    @ApiOperation(value = "update course")
    @PutMapping("/courses")
    @PreAuthorize("hasAuthority("+AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult updateCourse(@Valid @RequestBody Course course, BindingResult bindingResult){
        String message = "";
        boolean isSuccess = true;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            throw new BadRequestException(message);
        }else{
            try{
                boolean hasPermissionModify = this.courseService.checkPermissionModifyCourse(course.getCourseId());
                if(hasPermissionModify){
                    this.courseService.updateCourse(course);
                    message =  AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.COURSE);
                }else{
                    throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
                }
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.COURSE);
                isSuccess = false;
                Logger.getLogger(CourseController.class.getName()).log(Level.SEVERE,null,ex);
            }
        }
        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "get courses by lesson id")
    @GetMapping("/courses/lesson-id")
    @PreAuthorize("hasAuthority("+AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult getCoursesByLessonId(@RequestParam("lessonId") long lessonId){
        //check permission only author lesson can view course implement to lesson
        UserPrincipal userPrincipal = this.userService.getCurrentUser();
        long authorLesson = this.lessonService.getAuthorLessonByLessonId(lessonId);
        if(userPrincipal.getId() != authorLesson){
            throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
        }
        return new JsonResult("",this.courseService.getCourseDetailsByLessonId(lessonId));
    }

    @ApiOperation(value = "publish course")
    @PutMapping("/courses/publish")
    @PreAuthorize("hasAuthority("+AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult publishCourse(@Valid @RequestBody CoursePublishViewModel coursePublishViewModel
            , BindingResult bindingResult){
        String message = "";
        Boolean isSuccess = true;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            throw new BadRequestException(message);
        }else {
            boolean hasPermissionModify = this.courseService.checkPermissionUpdateStatusCourse(coursePublishViewModel.getCourseId());
            try{

                //Get course modify
                Course course = this.courseService.getCourseById(coursePublishViewModel.getCourseId())
                        .orElseThrow(() -> new ResourceNotFoundException("Course","id",coursePublishViewModel.getCourseId()));
                //check only author can update status
                if(hasPermissionModify){
                    this.courseService.publishCourse(coursePublishViewModel,course);
                    message = AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.COURSE);
                }else {
                    throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
                }
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.COURSE);
                isSuccess = false;
                Logger.getLogger(CourseController.class.getName()).log(Level.SEVERE,null,ex);
            }
        }

        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "Enroll course")
    @PostMapping("/courses/enroll")
    @PreAuthorize("hasAuthority("+AppRole.ROLE_LEARNER_AUTHENTICATIION+")")
    public @ResponseBody JsonResult enrollCourse(@RequestBody @Valid EnrollCourseViewModel enrollCourseViewModel, BindingResult bindingResult){
        String message = "";
        boolean isSuccess = true;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            throw new BadRequestException(message);
        }else{
            Course course = this.courseService.getCourseById(enrollCourseViewModel.getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course","id",enrollCourseViewModel.getCourseId()));
            UserPrincipal userPrincipal = this.userService.getCurrentUser();
            int userElo = this.userService.getPointByUserId(userPrincipal.getId());
            if(EloRatingLevel.getIdByEloRange(userElo) < course.getRequiredElo()){
                throw new AccessDeniedException(AppMessage.ELO_DENY_MESSAGE);
            }
            boolean isEnrolled = this.userHasCourseService.isEnrolled(course.getCourseId(),userPrincipal.getId());
            if(course.getStatusId() == Status.COURSE_STATUS_PUBLISHED && !isEnrolled){
                try {
                    this.courseService.enrollCourse(userPrincipal.getId(),course);
                    this.suggestionAlgorithmService.executeItemFilterSuggestionAlgorithm(course,userElo,userPrincipal.getId());
                    message = AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.ENROLL);
                }catch (DataIntegrityViolationException ex){
                    message = AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.ENROLL);
                    isSuccess = false;
                    Logger.getLogger(CourseController.class.getName()).log(Level.SEVERE,null,ex);
                }
            }else {
                message = AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.ENROLL);
                isSuccess = false;
            }
        }
        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "get course paginations by category id")
    @GetMapping("/courses/category-id")
    public @ResponseBody JsonResult getCoursePaginationByCategoryId(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize
            ,@RequestParam("categoryId") long categoryId,String statusId,String nameCourse,String sortBy,String sortDirection){

        if(nameCourse == null){
            nameCourse = "";
        }
        if(sortDirection == null){
            sortDirection = "";
        }
        if(sortBy == null){
            sortBy = "";
        }
        if(statusId == null){
            statusId = "";
        }
        PagedList<CoursePaginationViewModel> data = null;
        try{
            UserPrincipal userPrincipal = this.userService.getCurrentUser();
            long userId = 0;
            if(userPrincipal != null){
                userId = userPrincipal.getId();
            }
            data = courseService.getCoursePaginationsByCategoryId(nameCourse,statusId,page,pageSize,categoryId,userId,sortBy,sortDirection);
        }catch (IllegalArgumentException ex){
            Logger.getLogger(CourseController.class.getName()).log(Level.SEVERE,null,ex);
            throw new ResourceNotFoundException("Page","number",page);
        }

        return new JsonResult(null,data);
    }

    @ApiOperation(value = "get course paginations by elo id")
    @GetMapping("/courses/elo-id")
    public @ResponseBody JsonResult getCoursePaginationByEloId(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize
            ,@RequestParam("eloId") int eloId,String statusId,String nameCourse,String sortBy,String sortDirection){

        if(nameCourse == null){
            nameCourse = "";
        }
        if(sortDirection == null){
            sortDirection = "";
        }
        if(sortBy == null){
            sortBy = "";
        }
        if(statusId == null){
            statusId = "";
        }
        PagedList<CoursePaginationViewModel> data = null;
        try{
            UserPrincipal userPrincipal = this.userService.getCurrentUser();
            long userId = 0;
            if(userPrincipal != null){
                userId = userPrincipal.getId();
            }
            data = courseService.getCoursePaginationsByEloId(nameCourse,statusId,page,pageSize,eloId,userId,sortBy,sortDirection);
        }catch (IllegalArgumentException ex){
            Logger.getLogger(CourseController.class.getName()).log(Level.SEVERE,null,ex);
            throw new ResourceNotFoundException("Page","number",page);
        }

        return new JsonResult(null,data);
    }

    @ApiOperation(value = "Get review pagination")
    @GetMapping("/courses/reviews")
    public @ResponseBody JsonResult getReviewPagination(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize
            ,@RequestParam("courseId") long courseId){
        return new JsonResult(null,this.reviewService.getReviewPaginationByCourse(page,pageSize,courseId));
    }

    @ApiOperation(value = "Get course overview")
    @GetMapping("/courses/overview")
    public @ResponseBody JsonResult getCourseOverView(@RequestParam("courseId") long courseId){
        return new JsonResult(null,this.reviewService.getCourseOverview(courseId));
    }

    @ApiOperation(value = "Review on course")
    @PostMapping("/courses/review")
    @PreAuthorize("hasAuthority("+AppRole.ROLE_LEARNER_AUTHENTICATIION+")")
    public @ResponseBody JsonResult createReview(@RequestBody @Valid ReviewCreateViewModel reviewCreateViewModel,BindingResult bindingResult){
        String message = "";
        boolean isSuccess = true;
        long savedId = 0;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            throw new BadRequestException(message);
        }else{
            try{
                UserPrincipal userPrincipal = this.userService.getCurrentUser();
                User user = this.userService.getUserById(userPrincipal.getId()).get();
                if(this.reviewService.checkIsComment(user.getUserId(),reviewCreateViewModel.getCourseId())){
                    throw new NotAcceptedDeniedException(AppMessage.NOT_ACCEPTED_MESSAGE);
                }
                if(!this.courseService.checkPermissionReviewCourse(reviewCreateViewModel.getCourseId(),userPrincipal)){
                    throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
                }
                savedId = this.reviewService.create(reviewCreateViewModel,user.getUserId(),user.getAvatar(),user.getFullName());
                message =  AppMessage.getMessageSuccess(AppMessage.CREATE,AppMessage.REVIEW);
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.CREATE,AppMessage.REVIEW);
                isSuccess = false;
                Logger.getLogger(CourseController.class.getName()).log(Level.SEVERE,null,ex);
            }
        }

        CreateResponse createResponse = new CreateResponse();
        createResponse.setSuccess(isSuccess);
        createResponse.setSavedId(savedId);
        return new JsonResult(message,createResponse);
    }

    @ApiOperation(value = "update review on course")
    @PutMapping("/courses/review")
    @PreAuthorize("hasAuthority("+AppRole.ROLE_LEARNER_AUTHENTICATIION+")")
    public @ResponseBody JsonResult updateReview(@RequestBody @Valid ReviewUpdateViewModel reviewUpdateViewModel,BindingResult bindingResult){
        String message = "";
        boolean isSuccess = true;

        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            throw new BadRequestException(message);
        }else{
            try{
                if(!this.reviewService.isExist(reviewUpdateViewModel.getReviewId())){
                    throw new ResourceNotFoundException("Review","id",reviewUpdateViewModel.getReviewId());
                }

                UserPrincipal userPrincipal = this.userService.getCurrentUser();
                if(!this.reviewService.checkPermissionModifyReview(reviewUpdateViewModel.getReviewId()
                        ,userPrincipal.getId(),reviewUpdateViewModel.getCourseId())){
                    throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
                }
                this.reviewService.update(reviewUpdateViewModel);
                message =  AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.REVIEW);
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.REVIEW);
                isSuccess = false;
                Logger.getLogger(CourseController.class.getName()).log(Level.SEVERE,null,ex);
            }
        }
        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "remove review on course")
    @DeleteMapping("/courses/review/{review-id}")
    @PreAuthorize("hasAuthority("+AppRole.ROLE_LEARNER_AUTHENTICATIION+")")
    public @ResponseBody JsonResult removeReview(@PathVariable("review-id") long reviewId){
        String message = "";
        boolean isSuccess = true;

        try{
            Review review = this.reviewService.getById(reviewId)
                    .orElseThrow(() -> new ResourceNotFoundException("Review","id",reviewId));

            UserPrincipal userPrincipal = this.userService.getCurrentUser();
            if(!this.reviewService.checkPermissionModifyReview(review.getReviewId()
                    ,userPrincipal.getId(),review.getCourse().getCourseId())){
                throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
            }
            this.reviewService.remove(reviewId);
            message =  AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.REVIEW);
        }catch (DataIntegrityViolationException ex){
            message = AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.REVIEW);
            isSuccess = false;
            Logger.getLogger(CourseController.class.getName()).log(Level.SEVERE,null,ex);
        }
        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "Get course by user id")
    @GetMapping("/courses/userid")
    public @ResponseBody JsonResult getCoursePaginationsByUserId(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize,
            @RequestParam("userId") long userId,String statusId,String nameCourse,String sortBy,String sortDirection){
        if(sortDirection == null){
            sortDirection = "";
        }
        if(nameCourse == null){
            nameCourse = "";
        }
        if(statusId == null){
            statusId = "";
        }
        if(sortBy == null){
            sortBy = "";
        }
        PagedList<CoursePaginationViewModel> data = null;
        try{
            data = this.courseService.getCoursePaginationsByUserId(nameCourse,page,pageSize,userId,sortBy,sortDirection,statusId);
        }catch (IllegalArgumentException ex){
            Logger.getLogger(CourseController.class.getName()).log(Level.SEVERE,null,ex);
            throw new ResourceNotFoundException("Page","number",page);
        }

        return new JsonResult(null,data);
    }

    @ApiOperation(value = "Get course suggestion")
    @GetMapping("/courses/suggestion")
    @PreAuthorize("hasAuthority("+AppRole.ROLE_LEARNER_AUTHENTICATIION+")")
    public @ResponseBody JsonResult getCourseSuggestionPaginations(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize){
        PagedList<CoursePaginationViewModel> data = null;
        try{
            UserPrincipal userPrincipal = this.userService.getCurrentUser();
            data = this.courseService.getCourseSuggestion(page,pageSize,userPrincipal.getId());
        }catch (IllegalArgumentException ex){
            Logger.getLogger(CourseController.class.getName()).log(Level.SEVERE,null,ex);
            throw new ResourceNotFoundException("Page","number",page);
        }

        return new JsonResult(null,data);
    }

    @ApiOperation(value = "Get common course suggestion")
    @GetMapping("/courses/common-suggestion")
    public @ResponseBody JsonResult getCommonCourseSuggestionPaginations(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize
            ,@RequestParam("courseId")long courseId){
        PagedList<CoursePaginationViewModel> data = null;
        try{
            UserPrincipal userPrincipal = this.userService.getCurrentUser();
            long userId = 0;
            if(userPrincipal != null){
                userId = userPrincipal.getId();
            }
            data = this.courseService.getCommonCourseSuggestion(page,pageSize,courseId,userId);
        }catch (IllegalArgumentException ex){
            Logger.getLogger(CourseController.class.getName()).log(Level.SEVERE,null,ex);
            throw new ResourceNotFoundException("Page","number",page);
        }

        return new JsonResult(null,data);
    }
}
