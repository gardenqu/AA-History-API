package com.qjprojects.AA_History.Entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "word_bank_entry")
public class WordBankEntry {

    @Id
    @Column(name = "entry_id", length = 36, nullable = false, updatable = false)
    private String entryId = UUID.randomUUID().toString();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(length = 100, nullable = false)
    private String word;

    @OneToMany(mappedBy = "wordBankEntry", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<WordBankClue> clues = new ArrayList<>();

    public WordBankEntry() {}

    public WordBankEntry(Category category, String word) {
        this.category = category;
        this.word = word;
    }

    public String getEntryId() { return entryId; }
    public void setEntryId(String entryId) { this.entryId = entryId; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }

    public List<WordBankClue> getClues() { return clues; }
    public void setClues(List<WordBankClue> clues) { this.clues = clues; }
}