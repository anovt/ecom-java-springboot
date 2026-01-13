package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api") // to create api base path
public class CategoryController {

    @Autowired
    CategoryServiceImp categoriesService = new CategoryServiceImp();

    //    public CategoryController(CategoryServiceImp categoriesService) {
    //        this.categoriesService = categoriesService;
    //    }
    //@GetMapping("/public/categories")
    @RequestMapping(value="/public/categories",method = RequestMethod.GET)
    public ResponseEntity<CategoryResponse> getAllCategories(@RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                                             @RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                                                             @RequestParam(name="sortBy", defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
                                                             @RequestParam(name="sortOrder", defaultValue = AppConstants.SORT_ORDER,required = false) String sortOrder){

        CategoryResponse allCategories = categoriesService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(allCategories,HttpStatus.OK);
    }
    @PostMapping("/public/categories")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody Category category){

        CategoryResponse saveCategory = categoriesService.createCategory(category);
        return new ResponseEntity<CategoryResponse>(saveCategory,HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
        try{
            String status = categoriesService.deleteCategory(categoryId);
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(),e.getStatusCode());
        }

    }

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@RequestBody Category category,@PathVariable Long categoryId){
        try{
            categoriesService.updateCategory(category,categoryId);
            return new ResponseEntity<>("Category Update Successfully with "+categoryId,HttpStatus.CREATED);
        }catch (ResponseStatusException e){
            return new ResponseEntity<>(e.getReason(),e.getStatusCode());
        }

    }
}
