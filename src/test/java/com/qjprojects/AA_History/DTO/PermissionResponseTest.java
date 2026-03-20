package com.qjprojects.AA_History.DTO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PermissionResponseTest {
    @Test
    void constructorSetsFieldsCorrectly() {
        PermissionResponse dto = new PermissionResponse(
                "perm123",
                "PUZZLE_READ",
                "Allows reading puzzles",
                "PUZZLE",
                "READ"
        );

        assertEquals("perm123", dto.getPermissionId());
        assertEquals("PUZZLE_READ", dto.getName());
        assertEquals("Allows reading puzzles", dto.getDescription());
        assertEquals("PUZZLE", dto.getResource());
        assertEquals("READ", dto.getAction());
    }

}
