package com.qjprojects.AA_History.DTO;

import java.util.List;

public class WordBankEntryCreateRequest {

    private String categoryId;
    private String word;
    private List<String> clues;

    public WordBankEntryCreateRequest(String categoryId, String word, List<String> clues) {
        this.categoryId = categoryId;
        this.word = word;
        this.clues = clues;
    }

    public String getCategoryId() { return categoryId; }
    public String getWord() { return word; }
    public List<String> getClues() { return clues; }
}