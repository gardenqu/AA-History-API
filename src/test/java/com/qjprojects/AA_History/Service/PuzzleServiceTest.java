package com.qjprojects.AA_History.Service;

import com.qjprojects.AA_History.DTO.PuzzleCreateRequest;
import com.qjprojects.AA_History.Entity.*;
import com.qjprojects.AA_History.Repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PuzzleServiceTest {

    @Mock private PuzzleRepository puzzleRepository;
    @Mock private ClueService clueService;
    @Mock private CategoryService categoryService;
    @Mock private WordBankEntryRepository wordBankEntryRepository;
    @Mock private WordBankClueService wordBankClueService;
    @Mock private ClueRepository clueRepository;

    @InjectMocks
    private PuzzleService puzzleService;

    private Puzzle puzzle;
    private PuzzleCreateRequest createRequest;
    private Category category;
    private WordBankEntry entry1;
    private WordBankEntry entry2;

    @BeforeEach
    void setUp() {
        puzzle = new Puzzle(
                "Test Puzzle",
                "Easy",
                "Admin",
                10,
                10,
                Map.of("cells", List.of())
        );

        createRequest = new PuzzleCreateRequest(
                "Test Puzzle",
                "Easy",
                "Admin",
                "Black History"
        );

        category = new Category("Black History");

        entry1 = new WordBankEntry();
        entry1.setWord("HARRIET");

        entry2 = new WordBankEntry();
        entry2.setWord("GARVEY");
    }

    @Test
    void getByIdReturnsPuzzle() {
        when(puzzleRepository.findById(puzzle.getPuzzleId())).thenReturn(Optional.of(puzzle));

        Puzzle result = puzzleService.getById(puzzle.getPuzzleId());

        assertNotNull(result);
        assertEquals(puzzle.getPuzzleId(), result.getPuzzleId());
        verify(puzzleRepository).findById(puzzle.getPuzzleId());
    }

    @Test
    void getByIdThrowsWhenNotFound() {
        when(puzzleRepository.findById(anyString())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                puzzleService.getById("nonexistent-id"));

        assertEquals("Puzzle not found", ex.getMessage());
    }

    @Test
    void getAllReturnsPuzzles() {
        when(puzzleRepository.findAll()).thenReturn(List.of(puzzle));

        List<Puzzle> result = puzzleService.getAll();

        assertEquals(1, result.size());
        assertEquals("Test Puzzle", result.get(0).getTitle());
        verify(puzzleRepository).findAll();
    }

    @Test
    void getAllReturnsEmptyWhenNoPuzzles() {
        when(puzzleRepository.findAll()).thenReturn(List.of());

        List<Puzzle> result = puzzleService.getAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void deleteThrowsWhenNotFound() {
        when(puzzleRepository.existsById(anyString())).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                puzzleService.delete("nonexistent-id"));

        assertEquals("Puzzle not found", ex.getMessage());
        verify(puzzleRepository, never()).deleteById(anyString());
    }

    @Test
    void deleteCallsRepositoryWhenFound() {
        when(puzzleRepository.existsById(puzzle.getPuzzleId())).thenReturn(true);

        puzzleService.delete(puzzle.getPuzzleId());

        verify(puzzleRepository).deleteById(puzzle.getPuzzleId());
    }

    @Test
    void approveSetsStatusToApproved() {
        when(puzzleRepository.findById(puzzle.getPuzzleId())).thenReturn(Optional.of(puzzle));
        when(puzzleRepository.save(any(Puzzle.class))).thenReturn(puzzle);

        Puzzle result = puzzleService.approve(puzzle.getPuzzleId());

        assertEquals("APPROVED", result.getStatus());
        verify(puzzleRepository).save(puzzle);
    }

    @Test
    void rejectSetsStatusToRejected() {
        when(puzzleRepository.findById(puzzle.getPuzzleId())).thenReturn(Optional.of(puzzle));
        when(puzzleRepository.save(any(Puzzle.class))).thenReturn(puzzle);

        Puzzle result = puzzleService.reject(puzzle.getPuzzleId());

        assertEquals("REJECTED", result.getStatus());
        verify(puzzleRepository).save(puzzle);
    }
}