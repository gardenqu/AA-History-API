package com.qjprojects.AA_History.Entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CategoryEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void canPersistCategory() {
        Category category = new Category("Black History");

        Category saved = entityManager.persistFlushFind(category);

        assertNotNull(saved.getCategoryId());
        assertEquals("Black History", saved.getName());
    }

    @Test
    void nameMustBeUnique() {
        Category c1 = new Category("Black History");
        entityManager.persistAndFlush(c1);

        Category c2 = new Category("Black History");

        assertThrows(Exception.class, () -> {
            entityManager.persistAndFlush(c2);
        });
    }

    @Test
    void nameMustNotBeNull() {
        Category category = new Category(null);

        assertThrows(Exception.class, () -> {
            entityManager.persistAndFlush(category);
        });
    }

    @Test
    void wordBankEntriesInitializedEmpty() {
        Category category = new Category("Black Music");

        Category saved = entityManager.persistFlushFind(category);

        assertNotNull(saved.getWordBankEntries());
        assertTrue(saved.getWordBankEntries().isEmpty());
    }
}