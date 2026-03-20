package com.qjprojects.AA_History.DTO;

import java.util.List;

public class WordBankEntryResponse {

    private String entryId;
    private String categoryId;
    private String word;
    private List<String> clues;

    public WordBankEntryResponse(String entryId, String categoryId, String word, List<String> clues) {
        this.entryId = entryId;
        this.categoryId = categoryId;
        this.word = word;
        this.clues = clues;
    }

    public String getEntryId() { return entryId; }
    public String getCategoryId() { return categoryId; }
    public String getWord() { return word; }
    public List<String> getClues() { return clues; }
}