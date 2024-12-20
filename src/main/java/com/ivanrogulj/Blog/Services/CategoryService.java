package com.ivanrogulj.Blog.Services;

import com.ivanrogulj.Blog.DTO.CategoryDTO;
import com.ivanrogulj.Blog.Entities.Category;
import com.ivanrogulj.Blog.ExceptionHandler.DataNotFoundException;
import com.ivanrogulj.Blog.Repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {


    private final CategoryRepository categoryRepository;
    private final DTOAssembler dtoAssembler;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, DTOAssembler dtoAssembler) {
        this.categoryRepository = categoryRepository;
        this.dtoAssembler = dtoAssembler;
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
       Category category = dtoAssembler.convertDtoToCategory(categoryDTO);
       categoryDTO.setId(category.getId());
        categoryRepository.save(category);
        return categoryDTO;
    }

    public CategoryDTO findById(long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Category not found"));
        return dtoAssembler.convertToCategoryDto(category);
    }

    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Category not found"));
        if(!categoryDTO.getName().isEmpty())
        {
            category.setName(categoryDTO.getName());
        }
        return dtoAssembler.convertToCategoryDto(category);

    }

    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(dtoAssembler::convertToCategoryDto)
                .collect(Collectors.toList());
    }
    public void deleteCategory(long id) {
        Category category =categoryRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Category not found"));
        categoryRepository.delete(category);
    }
}