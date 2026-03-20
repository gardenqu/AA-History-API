package com.qjprojects.AA_History.Service;

import com.qjprojects.AA_History.Entity.Category;
import com.qjprojects.AA_History.Entity.WordBankClue;
import com.qjprojects.AA_History.Entity.WordBankEntry;
import com.qjprojects.AA_History.Repository.WordBankClueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WordBankClueServiceTest {

    @Mock
    private WordBankClueRepository wordBankClueRepository;

    @Mock
    private WordBankEntryService wordBankEntryService;

    @InjectMocks
    private WordBankClueService wordBankClueService;

    private WordBankEntry entry;
    private WordBankClue clue;

    @BeforeEach
    void setUp() {
        Category category = new Category("Black History");
        entry = new WordBankEntry(category, "HARRIET");
        clue = new WordBankClue(entry, "Led enslaved people to freedom");
    }

    @Test
    void createReturnsClue() {
        when(wordBankEntryService.getById(entry.getEntryId())).thenReturn(entry);
        when(wordBankClueRepository.save(any(WordBankClue.class)))
                .thenAnswer(i -> i.getArgument(0));

        WordBankClue result = wordBankClueService.create(
                entry.getEntryId(), "Led enslaved people to freedom");

        assertNotNull(result);
        assertEquals("Led enslaved people to freedom", result.getClueText());
        verify(wordBankClueRepository, times(1)).save(any(WordBankClue.class));
    }

    @Test
    void getByEntryReturnsClues() {
        when(wordBankClueRepository.findByWordBankEntryEntryId(entry.getEntryId()))
                .thenReturn(List.of(clue));

        List<WordBankClue> result = wordBankClueService.getByEntry(entry.getEntryId());

        assertEquals(1, result.size());
        assertEquals("Led enslaved people to freedom", result.get(0).getClueText());
    }

    @Test
    void pickRandomClueReturnsClueText() {
        when(wordBankClueRepository.findByWordBankEntryEntryId(entry.getEntryId()))
                .thenReturn(List.of(clue));

        String result = wordBankClueService.pickRandomClue(entry.getEntryId());

        assertEquals("Led enslaved people to freedom", result);
    }

    @Test
    void pickRandomClueThrowsWhenNoClues() {
        when(wordBankClueRepository.findByWordBankEntryEntryId(anyString()))
                .thenReturn(List.of());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                wordBankClueService.pickRandomClue("empty-entry-id"));

        assertEquals("No clues found for this word", ex.getMessage());
    }

    @Test
    void deleteRemovesClue() {
        when(wordBankClueRepository.existsById(clue.getWordBankClueId())).thenReturn(true);

        wordBankClueService.delete(clue.getWordBankClueId());

        verify(wordBankClueRepository, times(1)).deleteById(clue.getWordBankClueId());
    }

    @Test
    void deleteThrowsWhenNotFound() {
        when(wordBankClueRepository.existsById(anyString())).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                wordBankClueService.delete("nonexistent-id"));

        assertEquals("Clue not found", ex.getMessage());
        verify(wordBankClueRepository, never()).deleteById(any());
    }
}