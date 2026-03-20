package com.qjprojects.AA_History.Service;

import com.qjprojects.AA_History.Entity.Category;
import com.qjprojects.AA_History.Entity.WordBankEntry;
import com.qjprojects.AA_History.Repository.WordBankEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class WordBankEntryService {

    private final WordBankEntryRepository wordBankEntryRepository;
    private final CategoryService categoryService;
    private final Random random = new Random();

    public WordBankEntryService(WordBankEntryRepository wordBankEntryRepository,
                                CategoryService categoryService) {
        this.wordBankEntryRepository = wordBankEntryRepository;
        this.categoryService = categoryService;
    }

    public WordBankEntry create(String categoryId, String word) {
        Category category = categoryService.getById(categoryId);

        if (wordBankEntryRepository.existsByCategoryCategoryIdAndWord(categoryId, word)) {
            throw new RuntimeException("Word already exists in this category: " + word);
        }

        return wordBankEntryRepository.save(new WordBankEntry(category, word));
    }

    public WordBankEntry getById(String entryId) {
        return wordBankEntryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Word bank entry not found"));
    }

    public List<WordBankEntry> getByCategory(String categoryId) {
        return wordBankEntryRepository.findByCategoryCategoryId(categoryId);
    }

    public WordBankEntry getRandomEntryFromCategory(String categoryId) {
        List<WordBankEntry> entries = getByCategory(categoryId);
        if (entries.isEmpty()) {
            throw new RuntimeException("No words found for category");
        }
        return entries.get(random.nextInt(entries.size()));
    }

    public void delete(String entryId) {
        if (!wordBankEntryRepository.existsById(entryId)) {
            throw new RuntimeException("Word bank entry not found");
        }
        wordBankEntryRepository.deleteById(entryId);
    }
}