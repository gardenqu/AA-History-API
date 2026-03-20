package com.qjprojects.AA_History.Entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AppUserEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private AppUser createUser() {
        // Use the constructor directly — password is already encoded by AuthService in real usage
        // For tests, just set the hash manually
        AppUser user = new AppUser();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPasswordHash("$2a$10$fakehashfortesting");
        return user;
    }


    @Test
    void canPersistUser() {
        AppUser user = createUser();

        AppUser saved = entityManager.persistFlushFind(user);

        assertNotNull(saved.getId());
        assertEquals("testuser", saved.getUsername());
        assertEquals("test@example.com", saved.getEmail());
        assertNotNull(saved.getPasswordHash());
        assertTrue(saved.getPasswordHash().startsWith("$2a$"));
    }

    @Test
    void emailMustBeUnique() {
        AppUser u1 = new AppUser();
        u1.setUsername("user1");
        u1.setEmail("same@example.com");
        u1.setPasswordHash("$2a$10$fakehashfortesting");
        entityManager.persist(u1);

        AppUser u2 = new AppUser();
        u2.setUsername("user2");
        u2.setEmail("same@example.com");
        u2.setPasswordHash("$2a$10$fakehashfortesting");

        assertThrows(Exception.class, () -> {
            entityManager.persistAndFlush(u2);
        });
    }
    @Test
    void failedLoginFieldsPersistCorrectly() {
        AppUser user = createUser();
        user.setFailedLoginAttempts(4);
        user.setLastFailedLogin(LocalDateTime.now());

        AppUser saved = entityManager.persistFlushFind(user);

        assertEquals(4, saved.getFailedLoginAttempts());
        assertNotNull(saved.getLastFailedLogin());
    }

    @Test
    void canUpdatePassword() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        AppUser user = createUser();
        AppUser saved = entityManager.persistFlushFind(user);

        String oldHash = saved.getPasswordHash();

        saved.setPassword("newPassword456", encoder);
        AppUser updated = entityManager.persistFlushFind(saved);

        assertNotEquals(oldHash, updated.getPasswordHash());
        assertTrue(updated.getPasswordHash().startsWith("$2a$"));
        assertNotNull(updated.getPasswordChangedAt());
    }
    @Test
    void defaultValuesAreSet() {
        AppUser user = createUser();
        AppUser saved = entityManager.persistFlushFind(user);

        assertTrue(saved.getActive());
        assertFalse(saved.getVerified());
        assertFalse(saved.getTwoFactorEnabled());
        assertEquals(0, saved.getFailedLoginAttempts());
        assertEquals(0, saved.getPuzzlesSolved());
        assertEquals(0, saved.getTotalTimeSpent());
        assertEquals(0, saved.getCurrentStreak());
        assertEquals(0, saved.getLongestStreak());
    }
}