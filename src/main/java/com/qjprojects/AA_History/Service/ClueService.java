package com.qjprojects.AA_History.Service;

import com.qjprojects.AA_History.DTO.ClueCreateRequest;
import com.qjprojects.AA_History.Entity.Category;
import com.qjprojects.AA_History.Entity.Clue;
import com.qjprojects.AA_History.Entity.Puzzle;
import com.qjprojects.AA_History.Entity.WordBankEntry;
import com.qjprojects.AA_History.Repository.CategoryRepository;
import com.qjprojects.AA_History.Repository.ClueRepository;
import com.qjprojects.AA_History.Repository.PuzzleRepository;
import com.qjprojects.AA_History.Repository.WordBankEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ClueService {

    private final ClueRepository clueRepository;
    private final PuzzleRepository puzzleRepository;
    private final CategoryService categoryService;
    private final WordBankEntryRepository wordBankEntryRepository;
    private final WordBankClueService wordBankClueService;

    public ClueService(ClueRepository clueRepository,
                       PuzzleRepository puzzleRepository,
                       CategoryService categoryService,
                       WordBankEntryRepository wordBankEntryRepository,
                       WordBankClueService wordBankClueService) {
        this.clueRepository = clueRepository;
        this.puzzleRepository = puzzleRepository;
        this.categoryService = categoryService;
        this.wordBankEntryRepository = wordBankEntryRepository;
        this.wordBankClueService = wordBankClueService;
    }

    public Clue create(ClueCreateRequest request) {
        Puzzle puzzle = puzzleRepository.findById(request.getPuzzleId())
                .orElseThrow(() -> new RuntimeException("Puzzle not found"));

        Clue clue = new Clue(
                puzzle,
                request.getNumber(),
                request.getDirection(),
                request.getClue(),
                request.getAnswer(),
                request.getRow(),
                request.getCol()
        );

        return clueRepository.save(clue);
    }

    public void generateCluesForPuzzle(Puzzle puzzle, String categoryName) {
        Category category = categoryService.getByName(categoryName);

        List<WordBankEntry> entries = wordBankEntryRepository
                .findByCategoryCategoryId(category.getCategoryId());

        int clueNumber = 1;

        for (WordBankEntry entry : entries) {
            String clueText = wordBankClueService.pickRandomClue(entry.getEntryId());

            Clue clue = new Clue(
                    puzzle,
                    clueNumber,
                    "Across",
                    clueText,
                    entry.getWord(),
                    null,
                    null
            );

            clueRepository.save(clue);
            clueNumber++;
        }
    }

    public List<Clue> getCluesForPuzzle(String puzzleId) {
        return clueRepository.findByPuzzlePuzzleId(puzzleId);
    }
}