package com.qjprojects.AA_History.DTO;

public class CategoryResponse {

    private String categoryId;
    private String name;
    private int wordCount;

    public CategoryResponse(String categoryId, String name, int wordCount) {
        this.categoryId = categoryId;
        this.name = name;
        this.wordCount = wordCount;
    }

    public String getCategoryId() { return categoryId; }
    public String getName() { return name; }
    public int getWordCount() { return wordCount; }
}