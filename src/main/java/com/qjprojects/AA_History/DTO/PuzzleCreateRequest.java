package com.qjprojects.AA_History.DTO;

import jakarta.validation.constraints.NotBlank;

public class PuzzleCreateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String difficulty;

    private String author;

    @NotBlank
    private String category;

    public PuzzleCreateRequest(String title, String difficulty, String author, String category) {
        this.title = title;
        this.difficulty = difficulty;
        this.author = author;
        this.category = category;
    }

    public String getTitle() { return title; }
    public String getDifficulty() { return difficulty; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
}