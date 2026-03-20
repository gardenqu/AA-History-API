package com.qjprojects.AA_History.DTO;

import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class AuthResponseTest {

    @Test
    void constructorSetsFieldsCorrectly() {
        AuthResponse dto = new AuthResponse(
                "jwt-token-123",
                "user123",
                "testuser",
                Set.of("USER"),
                false
        );

        assertEquals("jwt-token-123", dto.getToken());
        assertEquals("user123", dto.getUserId());
        assertEquals("testuser", dto.getUsername());
        assertEquals(Set.of("USER"), dto.getRoles());
        assertFalse(dto.isProfileComplete());
    }
}