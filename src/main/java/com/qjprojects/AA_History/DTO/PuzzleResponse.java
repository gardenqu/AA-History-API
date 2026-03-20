package com.qjprojects.AA_History.DTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class PuzzleResponse {
    private String puzzleId;
    private String title;
    private String difficulty;
    private String author;
    private Integer width;
    private Integer height;
    private Map<String, Object> grid;
    private String status;
    private LocalDateTime publishedAt;
    private List<ClueResponse> clues;

    public PuzzleResponse(String puzzleId, String title, String difficulty,
                          String author, Integer width, Integer height,
                          Map<String, Object> grid, String status,
                          LocalDateTime publishedAt, List<ClueResponse> clues) {
        this.puzzleId = puzzleId;
        this.title = title;
        this.difficulty = difficulty;
        this.author = author;
        this.width = width;
        this.height = height;
        this.grid = grid;
        this.status = status;
        this.publishedAt = publishedAt;
        this.clues = clues;
    }

    public String getPuzzleId() { return puzzleId; }
    public String getTitle() { return title; }
    public String getDifficulty() { return difficulty; }
    public String getAuthor() { return author; }
    public Integer getWidth() { return width; }
    public Integer getHeight() { return height; }
    public Map<String, Object> getGrid() { return grid; }
    public String getStatus() { return status; }
    public LocalDateTime getPublishedAt() { return publishedAt; }
    public List<ClueResponse> getClues() { return clues; }
}