package com.qjprojects.AA_History.Entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "word_bank_clue")
public class WordBankClue {

    @Id
    @Column(name = "word_bank_clue_id", length = 36, nullable = false, updatable = false)
    private String wordBankClueId = UUID.randomUUID().toString();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entry_id", nullable = false)
    private WordBankEntry wordBankEntry;

    @Column(length = 500, nullable = false)
    private String clueText;

    public WordBankClue() {}

    public WordBankClue(WordBankEntry wordBankEntry, String clueText) {
        this.wordBankEntry = wordBankEntry;
        this.clueText = clueText;
    }

    public String getWordBankClueId() { return wordBankClueId; }
    public void setWordBankClueId(String wordBankClueId) { this.wordBankClueId = wordBankClueId; }

    public WordBankEntry getWordBankEntry() { return wordBankEntry; }
    public void setWordBankEntry(WordBankEntry wordBankEntry) { this.wordBankEntry = wordBankEntry; }

    public String getClueText() { return clueText; }
    public void setClueText(String clueText) { this.clueText = clueText; }
}