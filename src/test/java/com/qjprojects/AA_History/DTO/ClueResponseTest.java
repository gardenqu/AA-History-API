package com.qjprojects.AA_History.DTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClueResponseTest {

    @Test
    void constructorSetsFieldsCorrectly() {
        ClueResponse dto = new ClueResponse(
                "clue123",
                "puzzle123",
                "Across",
                1,
                "A fruit",
                "APPLE"
        );

        assertEquals("clue123", dto.getClueId());
        assertEquals("puzzle123", dto.getPuzzleId());
        assertEquals("Across", dto.getDirection());
        assertEquals(1, dto.getNumber());
        assertEquals("A fruit", dto.getClue());
        assertEquals("APPLE", dto.getAnswer());
    }

}
