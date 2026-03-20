package com.qjprojects.AA_History.Entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SecurityLogEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void canPersistSecurityLog() {
        // AppUser requires username, email, password
        AppUser user = new AppUser(
                "testuser",
                "test@example.com",
                "password123"
        );

        entityManager.persist(user);

        SecurityLog log = new SecurityLog(
                user,
                "LOGIN_SUCCESS",
                LocalDateTime.now(),
                "127.0.0.1"
        );

        SecurityLog saved = entityManager.persistFlushFind(log);

        assertNotNull(saved.getLogId());
        assertEquals("LOGIN_SUCCESS", saved.getEventType());
        assertEquals("127.0.0.1", saved.getIpAddress());
        assertEquals(user.getId(), saved.getUser().getId());
    }

    @Test
    void securityLogRequiresUserAndEventType() {
        SecurityLog log = new SecurityLog();
        log.setIpAddress("127.0.0.1");

        assertThrows(Exception.class, () -> {
            entityManager.persistAndFlush(log);
        });
    }
}