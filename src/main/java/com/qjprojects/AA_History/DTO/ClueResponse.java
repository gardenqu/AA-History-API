package com.qjprojects.AA_History.DTO;

public class ClueResponse {
    private String clueId;
    private String puzzleId;
    private String direction;
    private Integer number;
    private String clue;
    private String answer;
    private Integer row;
    private Integer col;

    public ClueResponse(String clueId, String puzzleId, String direction, Integer number,
                        String clue, String answer, Integer row, Integer col) {
        this.clueId = clueId;
        this.puzzleId = puzzleId;
        this.direction = direction;
        this.number = number;
        this.clue = clue;
        this.answer = answer;
        this.row = row;
        this.col = col;
    }

    public Integer getRow() { return row; }
    public Integer getCol() { return col; }

    public String getClueId() {
        return clueId;
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
