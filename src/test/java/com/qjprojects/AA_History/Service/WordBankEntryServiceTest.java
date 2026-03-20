package com.qjprojects.AA_History.Service;

import com.qjprojects.AA_History.Entity.Category;
import com.qjprojects.AA_History.Entity.WordBankEntry;
import com.qjprojects.AA_History.Repository.WordBankEntryRepository;
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
public class WordBankEntryServiceTest {

    @Mock
    private WordBankEntryRepository wordBankEntryRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private WordBankEntryService wordBankEntryService;

    private Category category;
    private WordBankEntry entry;

    @BeforeEach
    void setUp() {
        category = new Category("Black History");
        entry = new WordBankEntry(category, "HARRIET");
    }

    @Test
    void createReturnsEntry() {
        when(categoryService.getById(category.getCategoryId())).thenReturn(category);
        when(wordBankEntryRepository.existsByCategoryCategoryIdAndWord(
                category.getCategoryId(), "HARRIET")).thenReturn(false);
        when(wordBankEntryRepository.save(any(WordBankEntry.class)))
                .thenAnswer(i -> i.getArgument(0));

        WordBankEntry result = wordBankEntryService.create(category.getCategoryId(), "HARRIET");

        assertNotNull(result);
        assertEquals("HARRIET", result.getWord());
        verify(wordBankEntryRepository, times(1)).save(any(WordBankEntry.class));
    }

    @Test
    void createThrowsWhenWordAlreadyExists() {
        when(categoryService.getById(category.getCategoryId())).thenReturn(category);
        when(wordBankEntryRepository.existsByCategoryCategoryIdAndWord(
                category.getCategoryId(), "HARRIET")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                wordBankEntryService.create(category.getCategoryId(), "HARRIET"));

        assertTrue(ex.getMessage().contains("HARRIET"));
        verify(wordBankEntryRepository, never()).save(any());
    }

    @Test
    void getByIdReturnsEntry() {
        when(wordBankEntryRepository.findById(entry.getEntryId()))
                .thenReturn(Optional.of(entry));

        WordBankEntry result = wordBankEntryService.getById(entry.getEntryId());

        assertNotNull(result);
        assertEquals("HARRIET", result.getWord());
        verify(wordBankEntryRepository, times(1)).findById(entry.getEntryId());
    }

    @Test
    void getByIdThrowsWhenNotFound() {
        when(wordBankEntryRepository.findById(anyString())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                wordBankEntryService.getById("nonexistent-id"));

        assertEquals("Word bank entry not found", ex.getMessage());
    }

    @Test
    void getByCategoryReturnsEntries() {
        when(wordBankEntryRepository.findByCategoryCategoryId(category.getCategoryId()))
                .thenReturn(List.of(entry));

        List<WordBankEntry> result = wordBankEntryService.getByCategory(category.getCategoryId());

        assertEquals(1, result.size());
        assertEquals("HARRIET", result.get(0).getWord());
    }

    @Test
    void getRandomEntryFromCategoryReturnsEntry() {
        when(wordBankEntryRepository.findByCategoryCategoryId(category.getCategoryId()))
                .thenReturn(List.of(entry));

        WordBankEntry result = wordBankEntryService.getRandomEntryFromCategory(
                category.getCategoryId());

        assertNotNull(result);
        assertEquals("HARRIET", result.getWord());
    }

    @Test
    void getRandomEntryThrowsWhenCategoryEmpty() {
        when(wordBankEntryRepository.findByCategoryCategoryId(anyString()))
                .thenReturn(List.of());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                wordBankEntryService.getRandomEntryFromCategory("empty-category-id"));

        assertEquals("No words found for category", ex.getMessage());
    }

    @Test
    void deleteRemovesEntry() {
        when(wordBankEntryRepository.existsById(entry.getEntryId())).thenReturn(true);

        wordBankEntryService.delete(entry.getEntryId());

        verify(wordBankEntryRepository, times(1)).deleteById(entry.getEntryId());
    }

    @Test
    void deleteThrowsWhenNotFound() {
        when(wordBankEntryRepository.existsById(anyString())).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                wordBankEntryService.delete("nonexistent-id"));

        assertEquals("Word bank entry not found", ex.getMessage());
        verify(wordBankEntryRepository, never()).deleteById(any());
    }
}