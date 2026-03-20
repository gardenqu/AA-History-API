package com.qjprojects.AA_History.DTO;

import jakarta.validation.constraints.*;

public class ClueCreateRequest {
    @NotBlank
    private String puzzleId;

    @NotBlank
    private String direction; // "Across" or "Down"

    @Min(1)
    private Integer number;

    @NotBlank
    private String clue;

    @NotBlank
    private String answer;

    public ClueCreateRequest(String puzzleId, String direction, Integer number, String clue, String answer) {
        this.puzzleId = puzzleId;
        this.direction = direction;
        this.number = number;
        this.clue = clue;
        this.answer = answer;
    }

    public String getPuzzleId() {
        return puzzleId;
    }

    public String getDirection() {
        return direction;
    }

    public Integer getNumber() {
        return number;
    }

    public String getClue() {
        return clue;
    }

    public String getAnswer() {
        return answer;
    }
}
