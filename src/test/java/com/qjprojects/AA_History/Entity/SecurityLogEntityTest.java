package com.qjprojects.AA_History.Entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class SecurityLogEntityTest {

    @Test
    void constructorSetsFieldsCorrectly() {
        AppUser user = new AppUser("testuser", "test@example.com", "hashedpassword");
        LocalDateTime now = LocalDateTime.now();

        SecurityLog log = new SecurityLog(user, "USER_BANNED", now, "192.168.1.1");

        assertEquals(user, log.getUser());
        assertEquals("USER_BANNED", log.getEventType());
        assertEquals(now, log.getCreatedAt());
        assertEquals("192.168.1.1", log.getIpAddress());
    }

    @Test
    void defaultConstructorCreatesLog() {
        SecurityLog log = new SecurityLog();
        assertNotNull(log);
        assertNotNull(log.getLogId());
    }

    @Test
    void logIdIsGeneratedAutomatically() {
        SecurityLog log = new SecurityLog();
        assertNotNull(log.getLogId());
        assertFalse(log.getLogId().isEmpty());
    }

    @Test
    void twoLogsHaveDifferentIds() {
        SecurityLog l1 = new SecurityLog();
        SecurityLog l2 = new SecurityLog();
        assertNotEquals(l1.getLogId(), l2.getLogId());
    }

    @Test
    void settersWorkCorrectly() {
        SecurityLog log = new SecurityLog();
        AppUser user = new AppUser("testuser", "test@example.com", "hashedpassword");
        LocalDateTime now = LocalDateTime.now();

        log.setUser(user);
        log.setEventType("PASSWORD_CHANGED");
        log.setIpAddress("10.0.0.1");
        log.setUserAgent("PostmanRuntime/7.52.0");
        log.setDetails("Password changed successfully");
        log.setCreatedAt(now);

        assertEquals(user, log.getUser());
        assertEquals("PASSWORD_CHANGED", log.getEventType());
        assertEquals("10.0.0.1", log.getIpAddress());
        assertEquals("PostmanRuntime/7.52.0", log.getUserAgent());
        assertEquals("Password changed successfully", log.getDetails());
        assertEquals(now, log.getCreatedAt());
    }

    @Test
    void createdAtDefaultsToNow() {
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);
        SecurityLog log = new SecurityLog();
        LocalDateTime after = LocalDateTime.now().plusSeconds(1);

        assertTrue(log.getCreatedAt().isAfter(before));
        assertTrue(log.getCreatedAt().isBefore(after));
    }
}