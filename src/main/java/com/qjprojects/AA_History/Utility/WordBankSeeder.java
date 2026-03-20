package com.qjprojects.AA_History.Utility;

import com.qjprojects.AA_History.Entity.Category;
import com.qjprojects.AA_History.Entity.WordBankClue;
import com.qjprojects.AA_History.Entity.WordBankEntry;
import com.qjprojects.AA_History.Repository.CategoryRepository;
import com.qjprojects.AA_History.Repository.WordBankClueRepository;
import com.qjprojects.AA_History.Repository.WordBankEntryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public class WordBankSeeder {

    private final CategoryRepository categoryRepository;
    private final WordBankEntryRepository wordBankEntryRepository;
    private final WordBankClueRepository wordBankClueRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    public WordBankSeeder(CategoryRepository categoryRepository,
                          WordBankEntryRepository wordBankEntryRepository,
                          WordBankClueRepository wordBankClueRepository) {
        this.categoryRepository = categoryRepository;
        this.wordBankEntryRepository = wordBankEntryRepository;
        this.wordBankClueRepository = wordBankClueRepository;
    }

    @PostConstruct
    public void seed() {
        try {
            InputStream is = getClass()
                    .getClassLoader()
                    .getResourceAsStream("Utility/blackHistory.json");

            Map<String, Object> root = mapper.readValue(is, Map.class);
            List<Map<String, Object>> categories = (List<Map<String, Object>>) root.get("categories");

            for (Map<String, Object> categoryData : categories) {
                String categoryName = (String) categoryData.get("name");

                // Skip if already seeded
                if (categoryRepository.existsByName(categoryName)) {
                    continue;
                }

                Category category = categoryRepository.save(new Category(categoryName));

                List<Map<String, Object>> wordBank =
                        (List<Map<String, Object>>) categoryData.get("word_bank");

                for (Map<String, Object> wordData : wordBank) {
                    String word = (String) wordData.get("word");

                    WordBankEntry entry = wordBankEntryRepository.save(
                            new WordBankEntry(category, word)
                    );

                    List<String> clues = (List<String>) wordData.get("clues");

                    for (String clueText : clues) {
                        wordBankClueRepository.save(new WordBankClue(entry, clueText));
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to seed word bank", e);
        }
    }
}