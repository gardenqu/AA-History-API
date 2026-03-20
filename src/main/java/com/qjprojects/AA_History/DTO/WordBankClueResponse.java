package com.qjprojects.AA_History.DTO;

public class WordBankClueResponse {
    private String wordBankClueId;
    private String entryId;
    private String clueText;

    public WordBankClueResponse(String wordBankClueId, String entryId, String clueText) {
        this.wordBankClueId = wordBankClueId;
        this.entryId = entryId;
        this.clueText = clueText;
    }

    public String getWordBankClueId() { return wordBankClueId; }
    public String getEntryId() { return entryId; }
    public String getClueText() { return clueText; }
}