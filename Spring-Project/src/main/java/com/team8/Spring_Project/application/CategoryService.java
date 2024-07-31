package com.team8.Spring_Project.application;

import com.team8.Spring_Project.application.dto.CategoryDto;
import com.team8.Spring_Project.domain.Category;
import com.team8.Spring_Project.infrastructure.persistence.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryDto::fromEntity)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(CategoryDTO::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("데이터를 찾을 수 없습니다."));

    }

    public Category findCategoryEntity(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("데이터를 찾을 수 없습니다."));
    }


}
