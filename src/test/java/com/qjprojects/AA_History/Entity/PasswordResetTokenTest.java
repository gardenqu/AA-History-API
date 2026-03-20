package com.qjprojects.AA_History.Entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class PasswordResetTokenTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void tokenLinksToUser() {
        // Use the REAL constructor
        AppUser user = new AppUser(
                "resetuser",
                "reset@example.com",
                "secure"
        );

        entityManager.persist(user);

        PasswordResetToken token =
                new PasswordResetToken(user, LocalDateTime.now().plusMinutes(15));

        entityManager.persistFlushFind(token);

        assertEquals(user.getId(), token.getUser().getId());
    }
}

