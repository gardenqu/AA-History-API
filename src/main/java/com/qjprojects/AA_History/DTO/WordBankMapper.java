package com.qjprojects.AA_History.DTO;

import com.qjprojects.AA_History.Entity.Category;
import com.qjprojects.AA_History.Entity.WordBankClue;
import com.qjprojects.AA_History.Entity.WordBankEntry;

public class WordBankMapper {

    public static CategoryResponse toCategoryResponse(Category category) {
        return new CategoryResponse(
                category.getCategoryId(),
                category.getName(),
                category.getWordBankEntries().size()
        );
    }

    public static WordBankEntryResponse toWordBankEntryResponse(WordBankEntry entry) {
        return new WordBankEntryResponse(
                entry.getEntryId(),
                entry.getCategory().getCategoryId(),
                entry.getWord(),
                entry.getClues().stream()
                        .map(WordBankClue::getClueText)
                        .toList()
        );
    }
}