package com.qjprojects.AA_History.Entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class WordBankClueEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private WordBankEntry createEntry() {
        Category category = entityManager.persistFlushFind(new Category("Black Inventors"));
        return entityManager.persistFlushFind(new WordBankEntry(category, "CARVER"));
    }

    @Test
    void canPersistWordBankClue() {
        WordBankEntry entry = createEntry();
        WordBankClue clue = new WordBankClue(entry, "Known for agricultural innovations");

        WordBankClue saved = entityManager.persistFlushFind(clue);

        assertNotNull(saved.getWordBankClueId());
        assertEquals("Known for agricultural innovations", saved.getClueText());
        assertEquals(entry.getEntryId(), saved.getWordBankEntry().getEntryId());
    }

    @Test
    void clueTextMustNotBeNull() {
        WordBankEntry entry = createEntry();
        WordBankClue clue = new WordBankClue(entry, null);

        assertThrows(Exception.class, () -> {
            entityManager.persistAndFlush(clue);
        });
    }

    @Test
    void entryMustNotBeNull() {
        WordBankClue clue = new WordBankClue(null, "Some clue text");

        assertThrows(Exception.class, () -> {
            entityManager.persistAndFlush(clue);
        });
    }

    @Test
    void multipleCluesCanBelongToSameEntry() {
        WordBankEntry entry = createEntry();

        WordBankClue clue1 = new WordBankClue(entry, "Developed hundreds of uses for peanuts");
        WordBankClue clue2 = new WordBankClue(entry, "Promoted sustainable farming practices");
        WordBankClue clue3 = new WordBankClue(entry, "Known for agricultural innovations");

        entityManager.persistAndFlush(clue1);
        entityManager.persistAndFlush(clue2);
        WordBankClue saved = entityManager.persistFlushFind(clue3);

        assertEquals(entry.getEntryId(), saved.getWordBankEntry().getEntryId());
    }
}