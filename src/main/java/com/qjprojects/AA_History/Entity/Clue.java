package com.qjprojects.AA_History.Entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "clue")
public class Clue {


    @Id
    @Column(name = "clue_id",length=36, nullable = false, updatable = false)
    private String clueId= UUID.randomUUID().toString();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "puzzle_id", nullable = false)
    private Puzzle puzzle;

    @Column(nullable = false)
    private Integer number;

    @Column(length = 10, nullable = false)
    private String direction; // "Across" or "Down"

    @Column(name = "clue_text", length = 500, nullable = false)
    private String clueText;

    @Column(length = 100, nullable = false)
    private String answer;

    @Column
    private Integer row;

    @Column
    private Integer col;

    public Clue() {}

    public Clue(Puzzle puzzle, Integer number, String direction, String clueText, String answer, Integer row, Integer col) {
        this.puzzle = puzzle;
        this.number = number;
        this.direction = direction;
        this.clueText = clueText;
        this.answer = answer;
        this.row = row;
        this.col = col;
    }

    public Integer getRow() { return row; }
    public void setRow(Integer row) { this.row = row; }
    public Integer getCol() { return col; }
    public void setCol(Integer col) { this.col = col; }


    @Override
    public String toString() {
        return "<Clue " + number + " " + direction + ">";
    }

    //getters and setters


    public String getClueId() {
        return clueId;
    }

    public void setClueId(String clueId) {
        this.clueId = clueId;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getClueText() {
        return clueText;
    }

    public void setClueText(String clueText) {
        this.clueText = clueText;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
