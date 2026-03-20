package com.qjprojects.AA_History.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "puzzle_solve")
public class PuzzleSolve {

    @Id
    @Column(name = "solve_id", length = 36, nullable = false, updatable = false)
    private String solveId = UUID.randomUUID().toString();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "puzzle_id", nullable = false)
    private Puzzle puzzle;

    @Column(name = "time_taken", nullable = false)
    private Integer timeTaken; // seconds

    @Column(nullable = false)
    private Integer mistakes = 0;

    @Column(name = "used_hints", nullable = false)
    private Boolean usedHints = false;

    @Column(name = "solved_at", nullable = false, updatable = false)
    private LocalDateTime solvedAt = LocalDateTime.now();

    public PuzzleSolve() {}

    public PuzzleSolve(AppUser user, Puzzle puzzle, Integer timeTaken, Integer mistakes, Boolean usedHints) {
        this.user = user;
        this.puzzle = puzzle;
        this.timeTaken = timeTaken;
        this.mistakes = mistakes;
        this.usedHints = usedHints;
    }

    @Override
    public String toString() {
        return "<PuzzleSolve user=" + user.getId() + " puzzle=" + puzzle.getPuzzleId() + ">";
    }

    // getters and setters

    public String getSolveId() {
        return solveId;
    }

    public void setSolveId(String solveId) {
        this.solveId = solveId;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    public Integer getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Integer timeTaken) {
        this.timeTaken = timeTaken;
    }

    public Integer getMistakes() {
        return mistakes;
    }

    public void setMistakes(Integer mistakes) {
        this.mistakes = mistakes;
    }

    public Boolean getUsedHints() {
        return usedHints;
    }

    public void setUsedHints(Boolean usedHints) {
        this.usedHints = usedHints;
    }

    public LocalDateTime getSolvedAt() {
        return solvedAt;
    }

    public void setSolvedAt(LocalDateTime solvedAt) {
        this.solvedAt = solvedAt;
    }
}