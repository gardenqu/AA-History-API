package com.qjprojects.AA_History.DTO;

import jakarta.validation.constraints.NotBlank;

public class WordBankClueCreateRequest {

    @NotBlank
    private String entryId;

    @NotBlank
    private String clueText;

    public WordBankClueCreateRequest(String entryId, String clueText) {
        this.entryId = entryId;
        this.clueText = clueText;
    }

    public String getEntryId() { return entryId; }
    public String getClueText() { return clueText; }
}