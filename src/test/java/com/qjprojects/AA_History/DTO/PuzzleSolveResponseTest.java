package com.qjprojects.AA_History.DTO;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;



public class PuzzleSolveResponseTest {

    @Test
    void constructorSetsFieldsCorrectly() {
        LocalDateTime now = LocalDateTime.now();

        PuzzleSolveResponse dto = new PuzzleSolveResponse(
                "solve123",
                "puzzle123",
                "user123",
                90,
                1,
                false,
                now
        );

        assertEquals("solve123", dto.getSolveId());
        assertEquals("puzzle123", dto.getPuzzleId());
        assertEquals("user123", dto.getUserId());
        assertEquals(90, dto.getTimeTaken());
        assertEquals(1, dto.getMistakes());
        assertFalse(dto.isUsedHints());
        assertEquals(now, dto.getSolvedAt());
    }

}
