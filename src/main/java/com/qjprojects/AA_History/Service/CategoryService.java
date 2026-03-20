package com.qjprojects.AA_History.Service;

import com.qjprojects.AA_History.Entity.Category;
import com.qjprojects.AA_History.Repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category create(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new RuntimeException("Category already exists: " + name);
        }
        return categoryRepository.save(new Category(name));
    }

    public Category getById(String categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public Category getByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Category not found: " + name));
    }


    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public void delete(String categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new RuntimeException("Category not found");
        }
        categoryRepository.deleteById(categoryId);
    }
}