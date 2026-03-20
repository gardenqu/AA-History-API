package com.qjprojects.AA_History.DTO;

public class ClueData {
    private int number;
    private String direction;
    private String clueText;
    private String answer;
    private int row;
    private int col;

    public ClueData(int number, String direction, String clueText,
                    String answer, int row, int col) {
        this.number = number;
        this.direction = direction;
        this.clueText = clueText;
        this.answer = answer;
        this.row = row;
        this.col = col;
    }

    public int getNumber() { return number; }
    public String getDirection() { return direction; }
    public String getClueText() { return clueText; }
    public String getAnswer() { return answer; }
    public int getRow() { return row; }
    public int getCol() { return col; }
}