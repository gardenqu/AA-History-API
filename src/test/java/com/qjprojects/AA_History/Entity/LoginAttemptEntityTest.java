package com.qjprojects.AA_History.Entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class LoginAttemptEntityTest {

    @Test
    void constructorWithUserSetsFieldsCorrectly() {
        AppUser user = new AppUser("testuser", "test@example.com", "hashedpassword");
        LocalDateTime now = LocalDateTime.now();

        LoginAttempt attempt = new LoginAttempt(user, true, "192.168.1.1", now);

        assertEquals(user, attempt.getUser());
        assertEquals("test@example.com", attempt.getEmail());
        assertEquals("192.168.1.1", attempt.getIpAddress());
        assertTrue(attempt.isSuccess());
        assertEquals(now, attempt.getAttemptedAt());
        assertNull(attempt.getFailureReason());
    }

    @Test
    void constructorWithoutUserSetsFieldsCorrectly() {
        LocalDateTime now = LocalDateTime.now();

        LoginAttempt attempt = new LoginAttempt(
                "unknown@example.com",
                false,
                "10.0.0.1",
                "User not found",
                now
        );

        assertNull(attempt.getUser());
        assertEquals("unknown@example.com", attempt.getEmail());
        assertEquals("10.0.0.1", attempt.getIpAddress());
        assertFalse(attempt.isSuccess());
        assertEquals("User not found", attempt.getFailureReason());
        assertEquals(now, attempt.getAttemptedAt());
    }

    @Test
    void defaultConstructorCreatesAttempt() {
        LoginAttempt attempt = new LoginAttempt();
        assertNotNull(attempt);
        assertFalse(attempt.isSuccess());
    }

    @Test
    void settersWorkCorrectly() {
        LoginAttempt attempt = new LoginAttempt();
        LocalDateTime now = LocalDateTime.now();

        attempt.setEmail("test@example.com");
        attempt.setIpAddress("192.168.1.1");
        attempt.setUserAgent("PostmanRuntime/7.52.0");
        attempt.setSuccess(true);
        attempt.setFailureReason(null);
        attempt.setAttemptedAt(now);

        assertEquals("test@example.com", attempt.getEmail());
        assertEquals("192.168.1.1", attempt.getIpAddress());
        assertEquals("PostmanRuntime/7.52.0", attempt.getUserAgent());
        assertTrue(attempt.isSuccess());
        assertNull(attempt.getFailureReason());
        assertEquals(now, attempt.getAttemptedAt());
    }

    @Test
    void attemptIdIsGeneratedAutomatically() {
        LoginAttempt attempt = new LoginAttempt();
        assertNotNull(attempt.getAttemptID());
        assertFalse(attempt.getAttemptID().isEmpty());
    }

    @Test
    void twoAttemptsHaveDifferentIds() {
        LoginAttempt a1 = new LoginAttempt();
        LoginAttempt a2 = new LoginAttempt();
        assertNotEquals(a1.getAttemptID(), a2.getAttemptID());
    }
}