package com.ivanrogulj.Blog.Services;

import com.ivanrogulj.Blog.DTO.CategoryDTO;
import com.ivanrogulj.Blog.Entities.Category;
import com.ivanrogulj.Blog.Entities.Post;
import com.ivanrogulj.Blog.Repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {


    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        return categoryRepository.save(category);
    }

    public Category convertToCategoryEntity(CategoryDTO categoryDTO) {
        Category category = new Category();

        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());

        return category;
    }
}