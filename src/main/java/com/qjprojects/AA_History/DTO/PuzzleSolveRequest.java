package com.qjprojects.AA_History.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class PuzzleSolveRequest {
    @NotBlank
    private String puzzleId;

    @Min(0)
    private Integer timeTaken;

    @Min(0)
    private Integer mistakes;

    private boolean usedHints;

    private boolean completed;

    public PuzzleSolveRequest(String puzzleId, Integer timeTaken, Integer mistakes,
                              boolean usedHints, boolean completed) {
        this.puzzleId = puzzleId;
        this.timeTaken = timeTaken;
        this.mistakes = mistakes;
        this.usedHints = usedHints;
        this.completed = completed;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getPuzzleId() {
        return puzzleId;
    }

    public Integer getTimeTaken() {
        return timeTaken;
    }

    public Integer getMistakes() {
        return mistakes;
    }

    public boolean isUsedHints() {
        return usedHints;
    }
}
