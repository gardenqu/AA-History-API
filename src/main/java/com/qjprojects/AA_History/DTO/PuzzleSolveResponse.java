package com.qjprojects.AA_History.DTO;

import java.time.LocalDateTime;

public class PuzzleSolveResponse {
    private String solveId;
    private String puzzleId;
    private String userId;

    private Integer timeTaken;
    private Integer mistakes;
    private boolean usedHints;
    private LocalDateTime solvedAt;

    public PuzzleSolveResponse(String solveId, String puzzleId, String userId, Integer timeTaken, Integer mistakes, boolean usedHints, LocalDateTime solvedAt) {
        this.solveId = solveId;
        this.puzzleId = puzzleId;
        this.userId = userId;
        this.timeTaken = timeTaken;
        this.mistakes = mistakes;
        this.usedHints = usedHints;
        this.solvedAt = solvedAt;
    }

    public String getSolveId() {
        return solveId;
    }

    public String getPuzzleId() {
        return puzzleId;
    }

    public String getUserId() {
        return userId;
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

    public LocalDateTime getSolvedAt() {
        return solvedAt;
    }
}
