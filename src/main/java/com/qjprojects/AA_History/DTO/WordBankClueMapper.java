package com.qjprojects.AA_History.DTO;

import com.qjprojects.AA_History.Entity.WordBankClue;

public class WordBankClueMapper {

    public static WordBankClueResponse toResponse(WordBankClue clue) {
        return new WordBankClueResponse(
                clue.getWordBankClueId(),
                clue.getWordBankEntry().getEntryId(),
                clue.getClueText()
        );
    }
}