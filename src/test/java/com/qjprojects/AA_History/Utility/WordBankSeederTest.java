package com.qjprojects.AA_History.Utility;

import com.qjprojects.AA_History.Entity.Category;
import com.qjprojects.AA_History.Entity.WordBankClue;
import com.qjprojects.AA_History.Entity.WordBankEntry;
import com.qjprojects.AA_History.Repository.CategoryRepository;
import com.qjprojects.AA_History.Repository.WordBankClueRepository;
import com.qjprojects.AA_History.Repository.WordBankEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WordBankSeederTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private WordBankEntryRepository wordBankEntryRepository;

    @Mock
    private WordBankClueRepository wordBankClueRepository;

    @InjectMocks
    private WordBankSeeder wordBankSeeder;

    private int expectedCategories;
    private int expectedEntries;
    private int expectedClues;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        try {
            InputStream is = getClass()
                    .getClassLoader()
                    .getResourceAsStream("Utility/blackHistory.json");

            Map<String, Object> root = new ObjectMapper().readValue(is, Map.class);
            List<Map<String, Object>> categories =
                    (List<Map<String, Object>>) root.get("categories");

            expectedCategories = categories.size();

            for (Map<String, Object> category : categories) {
                List<Map<String, Object>> wordBank =
                        (List<Map<String, Object>>) category.get("word_bank");
                expectedEntries += wordBank.size();

                for (Map<String, Object> word : wordBank) {
                    List<String> clues = (List<String>) word.get("clues");
                    expectedClues += clues.size();
                }
            }
        } catch (Exception e) {
            fail("Could not load blackHistory.json for test setup: " + e.getMessage());
        }
    }

    @Test
    void seedSkipsIfAlreadySeeded() {
        when(categoryRepository.existsByName(anyString())).thenReturn(true);

        wordBankSeeder.seed();

        verify(categoryRepository, never()).save(any());
        verify(wordBankEntryRepository, never()).save(any());
        verify(wordBankClueRepository, never()).save(any());
    }

    @Test
    void seedPersistsCategoriesWhenNotSeeded() {
        when(categoryRepository.existsByName(anyString())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenAnswer(i -> i.getArgument(0));
        when(wordBankEntryRepository.save(any(WordBankEntry.class))).thenAnswer(i -> i.getArgument(0));
        when(wordBankClueRepository.save(any(WordBankClue.class))).thenAnswer(i -> i.getArgument(0));

        wordBankSeeder.seed();

        verify(categoryRepository, times(expectedCategories)).save(any(Category.class));
    }

    @Test
    void seedPersistsWordBankEntries() {
        when(categoryRepository.existsByName(anyString())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenAnswer(i -> i.getArgument(0));
        when(wordBankEntryRepository.save(any(WordBankEntry.class))).thenAnswer(i -> i.getArgument(0));
        when(wordBankClueRepository.save(any(WordBankClue.class))).thenAnswer(i -> i.getArgument(0));

        wordBankSeeder.seed();

        verify(wordBankEntryRepository, times(expectedEntries)).save(any(WordBankEntry.class));
    }

    @Test
    void seedPersistsClues() {
        when(categoryRepository.existsByName(anyString())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenAnswer(i -> i.getArgument(0));
        when(wordBankEntryRepository.save(any(WordBankEntry.class))).thenAnswer(i -> i.getArgument(0));
        when(wordBankClueRepository.save(any(WordBankClue.class))).thenAnswer(i -> i.getArgument(0));

        wordBankSeeder.seed();

        verify(wordBankClueRepository, times(expectedClues)).save(any(WordBankClue.class));
    }

    @Test
    void seedPartiallySkipsAlreadySeededCategories() {
        when(categoryRepository.existsByName(anyString())).thenReturn(false);
        // Skip the first category
        when(categoryRepository.existsByName("Black History")).thenReturn(true);

        when(categoryRepository.save(any(Category.class))).thenAnswer(i -> i.getArgument(0));
        when(wordBankEntryRepository.save(any(WordBankEntry.class))).thenAnswer(i -> i.getArgument(0));
        when(wordBankClueRepository.save(any(WordBankClue.class))).thenAnswer(i -> i.getArgument(0));

        wordBankSeeder.seed();

        // One less category saved
        verify(categoryRepository, times(expectedCategories - 1)).save(any(Category.class));
    }
}