package com.qjprojects.AA_History.DTO;

import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class UserResponseTest {

    @Test
    void constructorSetsFieldsCorrectly() {
        Set<String> roles = Set.of("USER", "ADMIN");

        UserResponse dto = new UserResponse(
                "user123",
                "testuser",
                "user@example.com",
                "Test User",
                "555-555-5555",
                "1990-01-01",
                true,
                true,
                false,
                false,
                roles
        );

        assertEquals("user123", dto.getId());
        assertEquals("testuser", dto.getUsername());
        assertEquals("user@example.com", dto.getEmail());
        assertEquals("Test User", dto.getName());
        assertEquals("555-555-5555", dto.getPhoneNumber());
        assertEquals("1990-01-01", dto.getBirthDate());
        assertTrue(dto.isVerified());
        assertTrue(dto.isActive());
        assertFalse(dto.isBanned());
        assertFalse(dto.isProfileComplete());
        assertEquals(roles, dto.getRoles());
    }
}