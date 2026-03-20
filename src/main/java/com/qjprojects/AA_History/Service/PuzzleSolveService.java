package com.qjprojects.AA_History.Service;

import com.qjprojects.AA_History.DTO.PuzzleSolveRequest;
import com.qjprojects.AA_History.Entity.AppUser;
import com.qjprojects.AA_History.Entity.Puzzle;
import com.qjprojects.AA_History.Entity.PuzzleSolve;
import com.qjprojects.AA_History.Repository.AppUserRepository;
import com.qjprojects.AA_History.Repository.PuzzleRepository;
import com.qjprojects.AA_History.Repository.PuzzleSolveRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PuzzleSolveService {

    private final PuzzleSolveRepository solveRepository;
    private final PuzzleRepository puzzleRepository;
    private final AppUserRepository userRepository;

    public PuzzleSolveService(
            PuzzleSolveRepository solveRepository,
            PuzzleRepository puzzleRepository,
            AppUserRepository userRepository
    ) {
        this.solveRepository = solveRepository;
        this.puzzleRepository = puzzleRepository;
        this.userRepository = userRepository;
    }

    public PuzzleSolve submitSolve(PuzzleSolveRequest request, String userId) {

        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Puzzle puzzle = puzzleRepository.findById(request.getPuzzleId())
                .orElseThrow(() -> new RuntimeException("Puzzle not found"));

        PuzzleSolve solve = new PuzzleSolve(
                user,
                puzzle,
                request.getTimeTaken(),
                request.getMistakes(),
                request.isUsedHints()
        );

        return solveRepository.save(solve);
    }

    public List<PuzzleSolve> getSolvesForUser(String userId) {
        return solveRepository.findByUserId(userId);
    }

    public List<PuzzleSolve> getSolvesForPuzzle(String puzzleId) {
        return solveRepository.findByPuzzlePuzzleId(puzzleId);
    }
}
