package com.qjprojects.AA_History.DTO;

import com.qjprojects.AA_History.Entity.Puzzle;

import java.util.List;

public class PuzzleMapper {

    public static PuzzleResponse toResponse(Puzzle puzzle, List<ClueResponse> clues) {
        return new PuzzleResponse(
                puzzle.getPuzzleId(),
                puzzle.getTitle(),
                puzzle.getDifficulty(),
                puzzle.getAuthor(),
                puzzle.getWidth(),
                puzzle.getHeight(),
                puzzle.getGrid(),
                puzzle.getStatus(),
                puzzle.getPublishedAt(),
                clues
        );
    }
}