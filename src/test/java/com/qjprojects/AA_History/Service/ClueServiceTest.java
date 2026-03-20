package com.qjprojects.AA_History.Service;

import com.qjprojects.AA_History.DTO.ClueCreateRequest;
import com.qjprojects.AA_History.Entity.Clue;
import com.qjprojects.AA_History.Entity.Puzzle;
import com.qjprojects.AA_History.Repository.ClueRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClueServiceTest {

    @Mock
    private ClueRepository clueRepository;

    @Mock
    private PuzzleRepository puzzleRepository;

    @Mock
    private MlClueGenerator mlClueGenerator;

    @InjectMocks
    private ClueService clueService;

    private Puzzle puzzle;
    private ClueCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        puzzle = new Puzzle();

        createRequest = new ClueCreateRequest(
                puzzle.getPuzzleId(),
                "Across",
                1,
                "A test clue",
                "ANSWER"
        );
    }

    @Test
    void createReturnsClue() {
        when(puzzleRepository.findById(puzzle.getPuzzleId())).thenReturn(Optional.of(puzzle));
        when(clueRepository.save(any(Clue.class))).thenAnswer(i -> i.getArgument(0));

        Clue result = clueService.create(createRequest);

        assertNotNull(result);
        assertEquals("Across", result.getDirection());
        assertEquals(1, result.getNumber());
        assertEquals("A test clue", result.getClueText());
        assertEquals("ANSWER", result.getAnswer());
        verify(clueRepository, times(1)).save(any(Clue.class));
    }

    @Test
    void createThrowsWhenPuzzleNotFound() {
        when(puzzleRepository.findById(anyString())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                clueService.create(createRequest));

        assertEquals("Puzzle not found", ex.getMessage());
        verify(clueRepository, never()).save(any());
    }

    @Test
    void generateCluesForPuzzleSavesOneCluePerEntry() {
        Map<String, String> wordClues = Map.of(
                "APPLE", "A fruit",
                "RIVER", "Flows to the sea"
        );

        when(mlClueGenerator.generateClues("Nature")).thenReturn(wordClues);
        when(clueRepository.save(any(Clue.class))).thenAnswer(i -> i.getArgument(0));

        clueService.generateCluesForPuzzle(puzzle, "Nature");

        verify(mlClueGenerator, times(1)).generateClues("Nature");
        verify(clueRepository, times(2)).save(any(Clue.class));
    }

    @Test
    void generateCluesForPuzzleSetsDirectionToAcross() {
        when(mlClueGenerator.generateClues("Nature")).thenReturn(Map.of("TREE", "Has leaves"));
        when(clueRepository.save(any(Clue.class))).thenAnswer(i -> i.getArgument(0));

        clueService.generateCluesForPuzzle(puzzle, "Nature");

        verify(clueRepository).save(argThat(clue -> clue.getDirection().equals("Across")));
    }

    @Test
    void generateCluesForPuzzleWithEmptyMapSavesNothing() {
        when(mlClueGenerator.generateClues("Empty")).thenReturn(Map.of());

        clueService.generateCluesForPuzzle(puzzle, "Empty");

        verify(clueRepository, never()).save(any());
    }

    @Test
    void getCluesForPuzzleReturnsFilteredClues() {
        Clue matchingClue = new Clue(puzzle, 1, "Across", "Correct clue", "ANSWER");

        when(clueRepository.findByPuzzlePuzzleId(puzzle.getPuzzleId()))
                .thenReturn(List.of(matchingClue));

        List<Clue> result = clueService.getCluesForPuzzle(puzzle.getPuzzleId());

        assertEquals(1, result.size());
        assertEquals("ANSWER", result.get(0).getAnswer());
    }

    @Test
    void getCluesForPuzzleReturnsEmptyWhenNoneMatch() {
        when(clueRepository.findByPuzzlePuzzleId(anyString())).thenReturn(List.of());

        List<Clue> result = clueService.getCluesForPuzzle("nonexistent-id");

        assertTrue(result.isEmpty());
    }

}