package com.qjprojects.AA_History.Service;

import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MlClueGenerator {

    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, List<Map<String, String>>> categoryMap = new HashMap<>();

    public MlClueGenerator() {
        loadDataset();
    }

    @SuppressWarnings("unchecked")
    private void loadDataset() {
        try {
            InputStream is = getClass()
                    .getClassLoader()
                    .getResourceAsStream("Utility/blackHistory.json");

            Map<String, Object> root = mapper.readValue(is, Map.class);

            List<Map<String, Object>> categories = (List<Map<String, Object>>) root.get("categories");

            for (Map<String, Object> category : categories) {
                String name = (String) category.get("name");
                List<Map<String, String>> wordBank =
                        (List<Map<String, String>>) category.get("word_bank");

                categoryMap.put(name, wordBank);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to load dataset.json", e);
        }
    }

    public Map<String, String> generateClues(String categoryName) {
        List<Map<String, String>> entries = categoryMap.get(categoryName);

        Map<String, String> clues = new HashMap<>();

        for (Map<String, String> entry : entries) {
            clues.put(entry.get("word"), entry.get("clue"));
        }

        return clues;
    }
}