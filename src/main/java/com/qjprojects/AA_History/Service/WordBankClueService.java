package com.qjprojects.AA_History.Service;

import com.qjprojects.AA_History.Entity.WordBankClue;
import com.qjprojects.AA_History.Entity.WordBankEntry;
import com.qjprojects.AA_History.Repository.WordBankClueRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class WordBankClueService {

    private final WordBankClueRepository wordBankClueRepository;
    private final WordBankEntryService wordBankEntryService;
    private final Random random = new Random();

    public WordBankClueService(WordBankClueRepository wordBankClueRepository,
                               WordBankEntryService wordBankEntryService) {
        this.wordBankClueRepository = wordBankClueRepository;
        this.wordBankEntryService = wordBankEntryService;
    }

    public WordBankClue create(String entryId, String clueText) {
        WordBankEntry entry = wordBankEntryService.getById(entryId);
        return wordBankClueRepository.save(new WordBankClue(entry, clueText));
    }

    public List<WordBankClue> getByEntry(String entryId) {
        return wordBankClueRepository.findByWordBankEntryEntryId(entryId);
    }

    public String pickRandomClue(String entryId) {
        List<WordBankClue> clues = getByEntry(entryId);
        if (clues.isEmpty()) {
            throw new RuntimeException("No clues found for this word");
        }
        return clues.get(random.nextInt(clues.size())).getClueText();
    }

    public void delete(String wordBankClueId) {
        if (!wordBankClueRepository.existsById(wordBankClueId)) {
            throw new RuntimeException("Clue not found");
        }
        wordBankClueRepository.deleteById(wordBankClueId);
    }
}