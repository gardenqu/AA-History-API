package com.qjprojects.AA_History.DTO;

import com.qjprojects.AA_History.Entity.PuzzleSolve;

public class PuzzleSolveMapper {

    public static PuzzleSolveResponse toResponse(PuzzleSolve solve) {
        return new PuzzleSolveResponse(
                solve.getSolveId(),
                solve.getPuzzle().getPuzzleId(),
                solve.getUser().getId(),
                solve.getTimeTaken(),
                solve.getMistakes(),
                solve.getUsedHints(),
                solve.getSolvedAt()
        );
    }
}