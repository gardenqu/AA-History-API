package com.qjprojects.AA_History.Entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoleEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void canPersistRole() {
        Role role = new Role("ADMIN", "Administrator role");

        Role saved = entityManager.persistFlushFind(role);

        assertNotNull(saved.getId());
        assertEquals("ADMIN", saved.getName());
    }

    @Test
    void roleNameMustBeUnique() {
        Role r1 = new Role("USER", "Standard user");
        entityManager.persistAndFlush(r1);

        Role r2 = new Role("USER", "Duplicate name");

        assertThrows(Exception.class, () -> {
            entityManager.persistAndFlush(r2);
        });
    }


    @Test
    void canAssignPermissionsToRole() {
        Permission p = new Permission();
        p.setName("EDIT_PUZZLE");
        p.setDescription("Edit puzzles");
        p.setResource("puzzle");
        p.setAction("edit");

        entityManager.persist(p);

        Role role = new Role("EDITOR", "Puzzle editor");
        role.setPermissions(Set.of(p));

        Role saved = entityManager.persistFlushFind(role);

        assertEquals(1, saved.getPermissions().size());
        assertTrue(saved.getPermissions().contains(p));
    }


}