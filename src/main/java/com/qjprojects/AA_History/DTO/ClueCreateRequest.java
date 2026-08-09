package com.qjprojects.AA_History.DTO;

import jakarta.validation.constraints.*;

public class ClueCreateRequest {
    @NotBlank
    private String puzzleId;

    @NotBlank
    private String direction;

    @Min(1)
    private Integer number;

    @NotBlank
    private String clue;

    @NotBlank
    private String answer;

    private Integer row;

    private Integer col;

    public ClueCreateRequest(String puzzleId, String direction, Integer number, String clue,
                             String answer, Integer row, Integer col) {
        this.puzzleId = puzzleId;
        this.direction = direction;
        this.number = number;
        this.clue = clue;
        this.answer = answer;
        this.row = row;
        this.col = col;
    }

    public String getPuzzleId() { return puzzleId; }
    public String getDirection() { return direction; }
    public Integer getNumber() { return number; }
    public String getClue() { return clue; }
    public String getAnswer() { return answer; }
    public Integer getRow() { return row; }
    public Integer getCol() { return col; }
}
