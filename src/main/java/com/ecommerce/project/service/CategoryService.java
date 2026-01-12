package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {

    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize);

    public CategoryResponse createCategory(Category category);

    public String deleteCategory(Long categoryId);

    public Category updateCategory(Category category,Long categoryId);
}
