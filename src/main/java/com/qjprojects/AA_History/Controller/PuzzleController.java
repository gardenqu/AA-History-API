package com.qjprojects.AA_History.Controller;

import com.qjprojects.AA_History.DTO.*;
import com.qjprojects.AA_History.Entity.AppUser;
import com.qjprojects.AA_History.Entity.Puzzle;
import com.qjprojects.AA_History.Entity.PuzzleSolve;
import com.qjprojects.AA_History.Service.ClueService;
import com.qjprojects.AA_History.Service.PuzzleService;
import com.qjprojects.AA_History.Service.PuzzleSolveService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/puzzles")
public class PuzzleController {

    private final PuzzleService puzzleService;
    private final PuzzleSolveService puzzleSolveService;
    private final ClueService clueService;

    public PuzzleController(PuzzleService puzzleService,
                            PuzzleSolveService puzzleSolveService,
                            ClueService clueService) {
        this.puzzleService = puzzleService;
        this.puzzleSolveService = puzzleSolveService;
        this.clueService = clueService;
    }

    @PostMapping
    public ResponseEntity<PuzzleResponse> create(
            @Valid @RequestBody PuzzleCreateRequest request) {
        Puzzle puzzle = puzzleService.create(request);
        List<ClueResponse> clues = clueService.getCluesForPuzzle(puzzle.getPuzzleId())
                .stream()
                .map(ClueMapper::toResponse)
                .toList();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PuzzleMapper.toResponse(puzzle, clues));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PuzzleResponse> getById(@PathVariable String id) {
        Puzzle puzzle = puzzleService.getById(id);
        List<ClueResponse> clues = clueService.getCluesForPuzzle(id)
                .stream()
                .map(ClueMapper::toResponse)
                .toList();
        return ResponseEntity.ok(PuzzleMapper.toResponse(puzzle, clues));
    }

    @GetMapping
    public ResponseEntity<List<PuzzleResponse>> getAll() {
        List<PuzzleResponse> puzzles = puzzleService.getAll()
                .stream()
                .map(puzzle -> {
                    List<ClueResponse> clues = clueService.getCluesForPuzzle(puzzle.getPuzzleId())
                            .stream()
                            .map(ClueMapper::toResponse)
                            .toList();
                    return PuzzleMapper.toResponse(puzzle, clues);
                })
                .toList();
        return ResponseEntity.ok(puzzles);
    }

    @PostMapping("/{id}/solve")
    public ResponseEntity<PuzzleSolveResponse> submitSolve(
            @PathVariable String id,
            @Valid @RequestBody PuzzleSolveRequest request,
            @AuthenticationPrincipal AppUser user) {
        PuzzleSolve solve = puzzleSolveService.submitSolve(request, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PuzzleSolveMapper.toResponse(solve));
    }

    @GetMapping("/solves/me")
    public ResponseEntity<List<PuzzleSolveResponse>> getMySolves(
            @AuthenticationPrincipal AppUser user) {
        List<PuzzleSolveResponse> solves = puzzleSolveService.getSolvesForUser(user.getId())
                .stream()
                .map(PuzzleSolveMapper::toResponse)
                .toList();
        return ResponseEntity.ok(solves);
    }

    @GetMapping("/{id}/solves")
    public ResponseEntity<List<PuzzleSolveResponse>> getSolvesForPuzzle(
            @PathVariable String id) {
        List<PuzzleSolveResponse> solves = puzzleSolveService.getSolvesForPuzzle(id)
                .stream()
                .map(PuzzleSolveMapper::toResponse)
                .toList();
        return ResponseEntity.ok(solves);
    }

    @GetMapping("/{id}/clues")
    public ResponseEntity<List<ClueResponse>> getCluesForPuzzle(@PathVariable String id) {
        List<ClueResponse> clues = clueService.getCluesForPuzzle(id)
                .stream()
                .map(ClueMapper::toResponse)
                .toList();
        return ResponseEntity.ok(clues);
    }
}