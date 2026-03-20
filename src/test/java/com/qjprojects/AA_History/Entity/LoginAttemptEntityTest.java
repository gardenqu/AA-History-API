package com.qjprojects.AA_History.Entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class LoginAttemptEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void canPersistLoginAttemptWithUser() {
        AppUser user = new AppUser();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPasswordHash("$2a$10$fakehashfortesting");  // ← changed

        entityManager.persist(user);

        LoginAttempt attempt = new LoginAttempt(
                user,
                false,
                "127.0.0.1",
                LocalDateTime.now()
        );

        LoginAttempt saved = entityManager.persistFlushFind(attempt);

        assertNotNull(saved);
        assertNotNull(saved.getUser());
        assertEquals(user.getId(), saved.getUser().getId());
        assertEquals("test@example.com", saved.getEmail());
        assertEquals("127.0.0.1", saved.getIpAddress());
        assertFalse(saved.isSuccess());
    }

    @Test
    void canPersistLoginAttemptWithoutUser() {
        LoginAttempt attempt = new LoginAttempt(
                "nouser@example.com",
                false,
                "192.168.1.10",
                "Invalid password",
                LocalDateTime.now()
        );

        LoginAttempt saved = entityManager.persistFlushFind(attempt);

        assertNotNull(saved.getAttemptID());
        assertNull(saved.getUser());
        assertEquals("nouser@example.com", saved.getEmail());
        assertEquals("Invalid password", saved.getFailureReason());
        assertEquals("192.168.1.10", saved.getIpAddress());
    }

    @Test
    void constructorCopiesEmailFromUser() {
        AppUser user = new AppUser();
        user.setUsername("copytest");
        user.setEmail("copy@example.com");
        user.setPasswordHash("$2a$10$fakehashfortesting");  // ← changed

        entityManager.persist(user);

        LoginAttempt attempt = new LoginAttempt(
                user,
                true,
                "10.0.0.1",
                LocalDateTime.now()
        );

        assertEquals("copy@example.com", attempt.getEmail());
    }

    @Test
    void missingTimestampShouldFail() {
        LoginAttempt attempt = new LoginAttempt(
                "missing@example.com",
                false,
                "10.0.0.2",
                "No timestamp",
                null
        );

        assertThrows(Exception.class, () -> {
            entityManager.persistAndFlush(attempt);
        });
    }
}