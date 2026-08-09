package com.qjprojects.AA_History.Service;

import com.qjprojects.AA_History.DTO.ClueData;
import com.qjprojects.AA_History.DTO.PuzzleCreateRequest;
import com.qjprojects.AA_History.Entity.Category;
import com.qjprojects.AA_History.Entity.Clue;
import com.qjprojects.AA_History.Entity.Puzzle;
import com.qjprojects.AA_History.Entity.WordBankEntry;
import com.qjprojects.AA_History.Repository.ClueRepository;
import com.qjprojects.AA_History.Repository.PuzzleRepository;
import com.qjprojects.AA_History.Repository.WordBankEntryRepository;
import com.qjprojects.AA_History.Utility.CrosswordGenerator;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PuzzleService {

    private final PuzzleRepository puzzleRepository;
    private final CategoryService categoryService;
    private final WordBankEntryRepository wordBankEntryRepository;
    private final WordBankClueService wordBankClueService;
    private final ClueRepository clueRepository;


    public PuzzleService(PuzzleRepository puzzleRepository,
                         CategoryService categoryService,
                         WordBankEntryRepository wordBankEntryRepository,
                         WordBankClueService wordBankClueService,
                         ClueRepository clueRepository) {
        this.puzzleRepository = puzzleRepository;
        this.categoryService = categoryService;
        this.wordBankEntryRepository = wordBankEntryRepository;
        this.wordBankClueService = wordBankClueService;
        this.clueRepository = clueRepository;
    }

    public Puzzle create(PuzzleCreateRequest request) {
        // Determine grid size and word count by difficulty
        int gridSize;
        int wordCount;
        switch (request.getDifficulty().toLowerCase()) {
            case "easy" -> { gridSize = 10; wordCount = 6; }
            case "hard" -> { gridSize = 20; wordCount = 15; }
            default -> { gridSize = 15; wordCount = 10; } // medium
        }

        // Fetch words from word bank
        Category category = categoryService.getByName(request.getCategory());
        List<WordBankEntry> entries = wordBankEntryRepository
                .findByCategoryCategoryId(category.getCategoryId());

        // Shuffle and limit to word count
        Collections.shuffle(entries);
        List<WordBankEntry> selected = entries.stream()
                .limit(wordCount)
                .toList();

        // Build word-clue pairs
        List<Map.Entry<String, String>> wordCluePairs = selected.stream()
                .map(entry -> Map.entry(
                        entry.getWord(),
                        wordBankClueService.pickRandomClue(entry.getEntryId())
                ))
                .toList();

        // Generate crossword grid
        CrosswordGenerator generator = new CrosswordGenerator(gridSize);
        generator.generate(wordCluePairs);

        // Convert grid to Map for storage
        String[][] gridArray = generator.getGrid();
        Map<String, Object> gridMap = new HashMap<>();
        gridMap.put("cells", gridArray);
        gridMap.put("size", gridSize);

        // Save puzzle
        Puzzle puzzle = new Puzzle(
                request.getTitle(),
                request.getDifficulty(),
                request.getAuthor(),
                gridSize,
                gridSize,
                gridMap
        );

        Puzzle saved = puzzleRepository.save(puzzle);

        // Save clues
        for (ClueData clueData : generator.getClues()) {
            Clue clue = new Clue(
                    saved,
                    clueData.getNumber(),
                    clueData.getDirection(),
                    clueData.getClueText(),
                    clueData.getAnswer(),
                    clueData.getRow(),
                    clueData.getCol()
            );
            clueRepository.save(clue);
        }

        return saved;
    }

    public Puzzle getById(String id) {
        return puzzleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Puzzle not found"));
    }

    public List<Puzzle> getAll() {
        return puzzleRepository.findAll();
    }

    public void delete(String id) {
        if (!puzzleRepository.existsById(id)) {
            throw new RuntimeException("Puzzle not found");
        }
        puzzleRepository.deleteById(id);
    }

    public Puzzle approve(String id) {
        Puzzle puzzle = getById(id);
        puzzle.setStatus("APPROVED");
        return puzzleRepository.save(puzzle);
    }

    public Puzzle reject(String id) {
        Puzzle puzzle = getById(id);
        puzzle.setStatus("REJECTED");
        return puzzleRepository.save(puzzle);
    }
}