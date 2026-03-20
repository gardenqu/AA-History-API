package com.qjprojects.AA_History.Service;

import com.qjprojects.AA_History.Entity.Category;
import com.qjprojects.AA_History.Repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category("Black History");
    }

    @Test
    void createReturnsCategory() {
        when(categoryRepository.existsByName("Black History")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenAnswer(i -> i.getArgument(0));

        Category result = categoryService.create("Black History");

        assertNotNull(result);
        assertEquals("Black History", result.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void createThrowsWhenCategoryAlreadyExists() {
        when(categoryRepository.existsByName("Black History")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                categoryService.create("Black History"));

        assertTrue(ex.getMessage().contains("Black History"));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void getByIdReturnsCategory() {
        when(categoryRepository.findById(category.getCategoryId()))
                .thenReturn(Optional.of(category));

        Category result = categoryService.getById(category.getCategoryId());

        assertNotNull(result);
        assertEquals("Black History", result.getName());
        verify(categoryRepository, times(1)).findById(category.getCategoryId());
    }

    @Test
    void getByIdThrowsWhenNotFound() {
        when(categoryRepository.findById(anyString())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                categoryService.getById("nonexistent-id"));

        assertEquals("Category not found", ex.getMessage());
    }

    @Test
    void getByNameReturnsCategory() {
        when(categoryRepository.findByName("Black History"))
                .thenReturn(Optional.of(category));

        Category result = categoryService.getByName("Black History");

        assertNotNull(result);
        assertEquals("Black History", result.getName());
        verify(categoryRepository, times(1)).findByName("Black History");
    }

    @Test
    void getByNameThrowsWhenNotFound() {
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                categoryService.getByName("Nonexistent Category"));

        assertTrue(ex.getMessage().contains("Nonexistent Category"));
    }

    @Test
    void getAllReturnsCategories() {
        Category category2 = new Category("Black Music");
        when(categoryRepository.findAll()).thenReturn(List.of(category, category2));

        List<Category> result = categoryService.getAll();

        assertEquals(2, result.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getAllReturnsEmptyWhenNoneExist() {
        when(categoryRepository.findAll()).thenReturn(List.of());

        List<Category> result = categoryService.getAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void deleteRemovesCategory() {
        when(categoryRepository.existsById(category.getCategoryId())).thenReturn(true);

        categoryService.delete(category.getCategoryId());

        verify(categoryRepository, times(1)).deleteById(category.getCategoryId());
    }

    @Test
    void deleteThrowsWhenNotFound() {
        when(categoryRepository.existsById(anyString())).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                categoryService.delete("nonexistent-id"));

        assertEquals("Category not found", ex.getMessage());
        verify(categoryRepository, never()).deleteById(any());
    }
}