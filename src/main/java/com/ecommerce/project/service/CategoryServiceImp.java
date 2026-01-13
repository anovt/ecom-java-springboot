package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.ApiResponse;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImp implements CategoryService {
    //private final List<Category> categories = new ArrayList<Category>();
    //long catId = 1L;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize,String sortBy,String sortOrder){

        Sort sortByOrder = sortOrder.equalsIgnoreCase("asc")
                ?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        List<Category> categories = categoryPage.getContent();

        List<CategoryDTO> categoryDTOS = categories.stream().map(category -> modelMapper.map(category,CategoryDTO.class)).toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent((!categoryDTOS.isEmpty()) ? categoryDTOS : Collections.emptyList());
        categoryResponse.setSuccess(true);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setTotalRecords(categoryPage.getTotalElements());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
        //return this.categories;
    }


    @Override
    public ApiResponse createCategory(Category category) {
        //category.setCategoryId(catId++);
        //categories.add(category);

        Category existingCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(existingCategory != null){
            throw new APIException("Category with name "+ category.getCategoryName() + " already exist");
        }
        Category savedCategory = categoryRepository.save(category);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(List.of(modelMapper.map(savedCategory,CategoryDTO.class)));
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Category Created successfully");
        return  apiResponse;

    }

    @Override
    public ApiResponse deleteCategory(Long categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

        Category savedCategory = categoryOptional.orElseThrow(()->new ResourceNotFoundException("Category","CategoryId",categoryId));

//        Category category = allCategories.stream().filter(c->c.getCategoryId().equals(categoryId))
//                .findFirst()
//                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found"));
                //.orElse(null);

//        if(category == null)
//        {
//            return "Category not found";
//        }

        //categories.remove(category);
        categoryRepository.delete(savedCategory);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Category with ID " +categoryId+ " is deleted");
        return apiResponse;

    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {

        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

        Category savedCategory = categoryOptional.orElseThrow(()->new ResourceNotFoundException("Category","CategoryId",categoryId));

        List<Category> existingCategory = categoryRepository.findByCategoryNameAndCategoryIdNot(category.getCategoryName(),categoryId);

        if(!existingCategory.isEmpty()) {

            throw new APIException("Category with name "+ category.getCategoryName() + " already exist");

        }

        category.setCategoryId(categoryId);

        categoryRepository.save(category);
        return savedCategory;

//        List <Category> allCategories = categoryRepository.findAll();
//        Optional<Category> optionalCategory = allCategories.stream().filter(c->c.getCategoryId().equals(categoryId))
//                .findFirst();
//
//        if(optionalCategory.isPresent()){
//
//            Category existingCategory = optionalCategory.get();
//            existingCategory.setCategoryName(category.getCategoryName());
//            categoryRepository.save(existingCategory);
//            return  existingCategory;
//
//        }else{
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found");
//        }

    }


}
