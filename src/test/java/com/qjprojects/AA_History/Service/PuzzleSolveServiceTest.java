package com.qjprojects.AA_History.Service;

import com.qjprojects.AA_History.DTO.PuzzleSolveRequest;
import com.qjprojects.AA_History.Entity.AppUser;
import com.qjprojects.AA_History.Entity.Puzzle;
import com.qjprojects.AA_History.Entity.PuzzleSolve;
import com.qjprojects.AA_History.Repository.AppUserRepository;
import com.qjprojects.AA_History.Repository.PuzzleRepository;
import com.qjprojects.AA_History.Repository.PuzzleSolveRepository;
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
public class PuzzleSolveServiceTest {

    @Mock
    private PuzzleSolveRepository solveRepository;

    @Mock
    private PuzzleRepository puzzleRepository;

    @Mock
    private AppUserRepository userRepository;

    @InjectMocks
    private PuzzleSolveService puzzleSolveService;

    private AppUser user;
    private Puzzle puzzle;
    private PuzzleSolveRequest solveRequest;

    @BeforeEach
    void setUp() {
        user = new AppUser();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPasswordHash("$2a$10$fakehashfortesting");

        puzzle = new Puzzle(
                "Test Puzzle",
                "Easy",
                "Admin",
                5,
                5,
                Map.of("0,0", "H")
        );

        solveRequest = new PuzzleSolveRequest(
                puzzle.getPuzzleId(),
                120,
                3,
                false
        );
    }

    @Test
    void submitSolveReturnsSolve() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(puzzleRepository.findById(puzzle.getPuzzleId())).thenReturn(Optional.of(puzzle));
        when(solveRepository.save(any(PuzzleSolve.class))).thenAnswer(i -> i.getArgument(0));

        PuzzleSolve result = puzzleSolveService.submitSolve(solveRequest, user.getId());

        assertNotNull(result);
        assertEquals(120, result.getTimeTaken());
        assertEquals(3, result.getMistakes());
        assertFalse(result.getUsedHints());
        verify(solveRepository, times(1)).save(any(PuzzleSolve.class));
    }

    @Test
    void submitSolveThrowsWhenUserNotFound() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                puzzleSolveService.submitSolve(solveRequest, "nonexistent-user"));

        assertEquals("User not found", ex.getMessage());
        verify(solveRepository, never()).save(any());
    }

    @Test
    void submitSolveThrowsWhenPuzzleNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(puzzleRepository.findById(anyString())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                puzzleSolveService.submitSolve(solveRequest, user.getId()));

        assertEquals("Puzzle not found", ex.getMessage());
        verify(solveRepository, never()).save(any());
    }

    @Test
    void getSolvesForUserReturnsSolves() {
        PuzzleSolve solve = new PuzzleSolve(user, puzzle, 120, 3, false);
        when(solveRepository.findByUserId(user.getId())).thenReturn(List.of(solve));

        List<PuzzleSolve> result = puzzleSolveService.getSolvesForUser(user.getId());

        assertEquals(1, result.size());
        assertEquals(120, result.get(0).getTimeTaken());
    }

    @Test
    void getSolvesForUserReturnsEmptyWhenNone() {
        when(solveRepository.findByUserId(anyString())).thenReturn(List.of());

        List<PuzzleSolve> result = puzzleSolveService.getSolvesForUser("no-solves-user");

        assertTrue(result.isEmpty());
    }

    @Test
    void getSolvesForPuzzleReturnsSolves() {
        PuzzleSolve solve = new PuzzleSolve(user, puzzle, 120, 3, false);
        when(solveRepository.findByPuzzlePuzzleId(puzzle.getPuzzleId()))
                .thenReturn(List.of(solve));

        List<PuzzleSolve> result = puzzleSolveService.getSolvesForPuzzle(puzzle.getPuzzleId());

        assertEquals(1, result.size());
        assertEquals(120, result.get(0).getTimeTaken());
    }

    @Test
    void getSolvesForPuzzleReturnsEmptyWhenNone() {
        when(solveRepository.findByPuzzlePuzzleId(anyString())).thenReturn(List.of());

        List<PuzzleSolve> result = puzzleSolveService.getSolvesForPuzzle("no-solves-puzzle");

        assertTrue(result.isEmpty());
    }
}