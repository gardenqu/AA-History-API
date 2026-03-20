package com.qjprojects.AA_History.Entity;


import com.qjprojects.AA_History.Utility.JsonMapConverter;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "puzzle")

public class Puzzle {

    @Id
    @Column(name = "puzzle_id", length = 36, nullable = false, updatable = false)
    private String puzzleId = UUID.randomUUID().toString();

    @Column(length = 200, nullable = false)
    private String title;

    @Column(length = 50, nullable = false)
    private String difficulty; // Easy, Medium, Hard

    @Column(length = 100)
    private String author;

    @Column(nullable = false)
    private Integer width;

    @Column(nullable = false)
    private Integer height;

    @Column(length = 20, nullable = false)
    private String status = "PENDING";

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }


    @Column(name = "grid", nullable = false, columnDefinition = "TEXT")
    @Convert(converter = JsonMapConverter.class)
    private Map<String, Object> grid;


    // Metadata
    @Column(name = "published_at", nullable = false, updatable = false)
    private LocalDateTime publishedAt = LocalDateTime.now();

    // Clues for this puzzle
    @OneToMany(mappedBy = "puzzle", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Clue> clues = new HashSet<>();

    // Solve history
    @OneToMany(mappedBy = "puzzle", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<PuzzleSolve> solves = new HashSet<>();

    public Puzzle() {}

    public Puzzle(String title, String difficulty, String author, Integer width, Integer height, Map<String, Object> grid) {
        this.title = title;
        this.difficulty = difficulty;
        this.author = author;
        this.width = width;
        this.height = height;
        this.grid = grid;
    }

    @Override
    public String toString() {
        return "<Puzzle " + title + ">";
    }

    //getter and setters


    public String getPuzzleId() {
        return puzzleId;
    }

    public void setPuzzleId(String puzzleId) {
        this.puzzleId = puzzleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Map<String, Object> getGrid() {
        return grid;
    }

    public void setGrid(Map<String, Object> grid) {
        this.grid = grid;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Set<Clue> getClues() {
        return clues;
    }

    public void setClues(Set<Clue> clues) {
        this.clues = clues;
    }

    public Set<PuzzleSolve> getSolves() {
        return solves;
    }

    public void setSolves(Set<PuzzleSolve> solves) {
        this.solves = solves;
    }
}
