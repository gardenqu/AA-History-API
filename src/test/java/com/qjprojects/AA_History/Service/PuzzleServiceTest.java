package com.qjprojects.AA_History.Service;

import com.qjprojects.AA_History.DTO.PuzzleCreateRequest;
import com.qjprojects.AA_History.Entity.Puzzle;
import com.qjprojects.AA_History.Repository.PuzzleRepository;
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

    @Mock
    private PuzzleRepository puzzleRepository;

    @Mock
    private ClueService clueService;

    @InjectMocks
    private PuzzleService puzzleService;

    private Puzzle puzzle;
    private PuzzleCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        puzzle = new Puzzle(
                "Test Puzzle",
                "Easy",
                "Admin",
                5,
                5,
                Map.of("0,0", "H", "0,1", "I")
        );

        createRequest = new PuzzleCreateRequest(
                "Test Puzzle",
                "Easy",
                "Admin",
                5,
                5,
                Map.of("0,0", "H", "0,1", "I"),
                "Black History"
        );
    }

    @Test
    void createReturnsPuzzle() {
        when(puzzleRepository.save(any(Puzzle.class))).thenReturn(puzzle);

        Puzzle result = puzzleService.create(createRequest);

        assertNotNull(result);
        assertEquals("Test Puzzle", result.getTitle());
        assertEquals("Easy", result.getDifficulty());
        verify(puzzleRepository, times(1)).save(any(Puzzle.class));
    }

    @Test
    void createGeneratesCluesForPuzzle() {
        when(puzzleRepository.save(any(Puzzle.class))).thenReturn(puzzle);

        puzzleService.create(createRequest);

        verify(clueService, times(1)).generateCluesForPuzzle(puzzle, "Black History");
    }

    @Test
    void getByIdReturnsPuzzle() {
        when(puzzleRepository.findById(puzzle.getPuzzleId())).thenReturn(Optional.of(puzzle));

        Puzzle result = puzzleService.getById(puzzle.getPuzzleId());

        assertNotNull(result);
        assertEquals(puzzle.getPuzzleId(), result.getPuzzleId());
        verify(puzzleRepository, times(1)).findById(puzzle.getPuzzleId());
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
        verify(puzzleRepository, times(1)).findAll();
    }

    @Test
    void getAllReturnsEmptyWhenNoPuzzles() {
        when(puzzleRepository.findAll()).thenReturn(List.of());

        List<Puzzle> result = puzzleService.getAll();

        assertTrue(result.isEmpty());
    }
}