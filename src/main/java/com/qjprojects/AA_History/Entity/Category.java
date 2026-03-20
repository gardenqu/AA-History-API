package com.qjprojects.AA_History.Entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @Column(name = "category_id", length = 36, nullable = false, updatable = false)
    private String categoryId = UUID.randomUUID().toString();

    @Column(length = 100, nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.EAGER)
    private List<WordBankEntry> wordBankEntries = new ArrayList<>();

    public Category() {}

    public Category(String name) {
        this.name = name;
    }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<WordBankEntry> getWordBankEntries() { return wordBankEntries; }
    public void setWordBankEntries(List<WordBankEntry> wordBankEntries) { this.wordBankEntries = wordBankEntries; }
}