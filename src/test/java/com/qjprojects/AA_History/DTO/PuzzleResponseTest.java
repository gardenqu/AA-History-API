package com.qjprojects.AA_History.DTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class PuzzleResponseTest {

    @Test
    void constructorSetsFieldsCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        Map<String, Object> grid = Map.of("0,0", "A");

        PuzzleResponse dto = new PuzzleResponse(
                grid,
                "p1",
                "Daily Mini",
                "Easy",
                "Author",
                5, 5,
                now
        );

        assertEquals("p1", dto.getPuzzleId());
        assertEquals("Daily Mini", dto.getTitle());
        assertEquals("Easy", dto.getDifficulty());
        assertEquals("Author", dto.getAuthor());
        assertEquals(5, dto.getWidth());
        assertEquals(5, dto.getHeight());
        assertEquals(grid, dto.getGrid());
        assertEquals(now, dto.getPublishedAt());
    }

}
