package com.chess.chessapi.controllers;

import com.chess.chessapi.constants.AppMessage;
import com.chess.chessapi.constants.AppRole;
import com.chess.chessapi.entities.Category;
import com.chess.chessapi.exceptions.BadRequestException;
import com.chess.chessapi.exceptions.ResourceNotFoundException;
import com.chess.chessapi.models.CreateResponse;
import com.chess.chessapi.models.JsonResult;
import com.chess.chessapi.services.CategoryService;
import com.chess.chessapi.viewmodels.CategoryViewModel;
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
@Api(value = "Category Management")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    @ApiOperation(value = "get categories")
    @GetMapping("/categories")
    public @ResponseBody JsonResult getCategories(){
        return new JsonResult(null,this.categoryService.getAllCategory());
    }

    @ApiOperation(value = "get category detail by id")
    @GetMapping("/categories/{id}")
    public @ResponseBody JsonResult getCategoryById(@PathVariable("id") long categoryId){
        Category category = this.categoryService.getCategoryById(categoryId);

        if(category == null){
            throw new ResourceNotFoundException("Category","id",categoryId);
        }

        return new JsonResult(null,category);
    }

    @ApiOperation(value = "remove category")
    @DeleteMapping("/categories/{id}")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_ADMIN_AUTHENTICATIION+")")
    public @ResponseBody JsonResult removeCategoryById(@PathVariable("id") long categoryId){
        Category category = this.categoryService.getCategoryById(categoryId);

        if(category == null){
            throw new ResourceNotFoundException("Category","id",categoryId);
        }

        Boolean isSuccess = true;
        String message = "";
        try{
            this.categoryService.removeCategory(category);
            message = AppMessage.getMessageSuccess(AppMessage.DELETE,AppMessage.CATEGORY);
        }catch (DataIntegrityViolationException ex){
            isSuccess = false;
            message =  AppMessage.getMessageFail(AppMessage.DELETE,AppMessage.CATEGORY);
            Logger.getLogger(CategoryController.class.getName()).log(Level.SEVERE,null,ex);
        }
        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "create category")
    @PostMapping("/categories")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_ADMIN_AUTHENTICATIION+")")
    public @ResponseBody JsonResult createCategory(@RequestBody @Valid CategoryViewModel category, BindingResult bindingResult){
        Boolean isSuccess = true;
        long savedId = 0;
        String message = "";
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            throw new BadRequestException(message);
        }else {
            try {
                savedId = this.categoryService.create(category);
                message = AppMessage.getMessageSuccess(AppMessage.CREATE, AppMessage.CATEGORY);
            } catch (DataIntegrityViolationException ex) {
                isSuccess = false;
                message = AppMessage.getMessageFail(AppMessage.CREATE, AppMessage.CATEGORY);
                Logger.getLogger(CategoryController.class.getName()).log(Level.SEVERE,null,ex);
            }
        }
        CreateResponse createResponse = new CreateResponse();
        createResponse.setSuccess(isSuccess);
        createResponse.setSavedId(savedId);
        return new JsonResult(message,createResponse);
    }

    @ApiOperation(value = "update category")
    @PutMapping("/categories")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_ADMIN_AUTHENTICATIION+")")
    public @ResponseBody JsonResult updateCategory(@RequestBody @Valid CategoryViewModel category, BindingResult bindingResult){

        Boolean isSuccess = true;
        String message = "";
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        }else {
            try {
                if(!this.categoryService.isExist(category.getCategoryId())){
                    throw new ResourceNotFoundException("Category","id",category.getCategoryId());
                }

                this.categoryService.update(category);
                message = AppMessage.getMessageSuccess(AppMessage.UPDATE, AppMessage.CATEGORY);
            } catch (DataIntegrityViolationException ex) {
                isSuccess = false;
                message = AppMessage.getMessageFail(AppMessage.UPDATE, AppMessage.CATEGORY);
                Logger.getLogger(CategoryController.class.getName()).log(Level.SEVERE,null,ex);
            }
        }
        return new JsonResult(message,isSuccess);
    }
}
