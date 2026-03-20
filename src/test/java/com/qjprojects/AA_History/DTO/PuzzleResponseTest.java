package com.qjprojects.AA_History.DTO;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class PuzzleResponseTest {

    @Test
    void constructorSetsFieldsCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        Map<String, Object> grid = Map.of("cells", List.of());

        PuzzleResponse dto = new PuzzleResponse(
                "p1",
                "Daily Mini",
                "Easy",
                "Author",
                5,
                5,
                grid,
                "PENDING",
                now,
                List.of()
        );

        assertEquals("p1", dto.getPuzzleId());
        assertEquals("Daily Mini", dto.getTitle());
        assertEquals("Easy", dto.getDifficulty());
        assertEquals("Author", dto.getAuthor());
        assertEquals(5, dto.getWidth());
        assertEquals(5, dto.getHeight());
        assertEquals(grid, dto.getGrid());
        assertEquals("PENDING", dto.getStatus());
        assertEquals(now, dto.getPublishedAt());
        assertTrue(dto.getClues().isEmpty());
    }
}