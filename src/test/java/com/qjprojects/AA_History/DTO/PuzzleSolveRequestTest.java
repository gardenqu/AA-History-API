package com.qjprojects.AA_History.DTO;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PuzzleSolveRequestTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void constructorSetsFieldsCorrectly() {
        PuzzleSolveRequest dto = new PuzzleSolveRequest(
                "puzzle123",
                90,
                1,
                false,
                false
        );

        assertEquals("puzzle123", dto.getPuzzleId());
        assertEquals(90, dto.getTimeTaken());
        assertEquals(1, dto.getMistakes());
        assertFalse(dto.isUsedHints());
    }

    @Test
    void validationFailsForBlankPuzzleId() {
        PuzzleSolveRequest dto = new PuzzleSolveRequest(
                "",
                90,
                1,
                false,
                false
        );

        assertFalse(validator.validate(dto).isEmpty());
    }


}
