package com.qjprojects.AA_History.Entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class WordBankEntryEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private Category createCategory() {
        return entityManager.persistFlushFind(new Category("Black Literature"));
    }

    @Test
    void canPersistWordBankEntry() {
        Category category = createCategory();
        WordBankEntry entry = new WordBankEntry(category, "HUGHES");

        WordBankEntry saved = entityManager.persistFlushFind(entry);

        assertNotNull(saved.getEntryId());
        assertEquals("HUGHES", saved.getWord());
        assertEquals(category.getCategoryId(), saved.getCategory().getCategoryId());
    }

    @Test
    void wordMustNotBeNull() {
        Category category = createCategory();
        WordBankEntry entry = new WordBankEntry(category, null);

        assertThrows(Exception.class, () -> {
            entityManager.persistAndFlush(entry);
        });
    }

    @Test
    void categoryMustNotBeNull() {
        WordBankEntry entry = new WordBankEntry(null, "HUGHES");

        assertThrows(Exception.class, () -> {
            entityManager.persistAndFlush(entry);
        });
    }

    @Test
    void cluesInitializedEmpty() {
        Category category = createCategory();
        WordBankEntry entry = new WordBankEntry(category, "MORRISON");

        WordBankEntry saved = entityManager.persistFlushFind(entry);

        assertNotNull(saved.getClues());
        assertTrue(saved.getClues().isEmpty());
    }
}