package com.qjprojects.AA_History.Entity;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PermissionEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void canPersistPermission() {
        Permission perm = new Permission();
        perm.setName("READ_PUZZLE");
        perm.setDescription("Allows reading puzzles");
        perm.setResource("puzzle");
        perm.setAction("read");

        Permission saved = entityManager.persistFlushFind(perm);

        assertNotNull(saved.getId());
        assertEquals("READ_PUZZLE", saved.getName());
        assertEquals("puzzle", saved.getResource());
        assertEquals("read", saved.getAction());
    }

    @Test
    void permissionMustHaveRequiredFields() {
        Permission perm = new Permission();

        assertThrows(Exception.class, () -> {
            entityManager.persistAndFlush(perm);
        });
    }
}
