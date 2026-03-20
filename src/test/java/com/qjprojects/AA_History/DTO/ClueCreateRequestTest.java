package com.qjprojects.AA_History.DTO;


import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClueCreateRequestTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void constructorSetsFieldsCorrectly() {
        ClueCreateRequest dto = new ClueCreateRequest(
                "puzzle123",
                "Across",
                1,
                "A fruit",
                "APPLE"
        );

        assertEquals("puzzle123", dto.getPuzzleId());
        assertEquals("Across", dto.getDirection());
        assertEquals(1, dto.getNumber());
        assertEquals("A fruit", dto.getClue());
        assertEquals("APPLE", dto.getAnswer());
    }

    @Test
    void validationFailsForBlankClue() {
        ClueCreateRequest dto = new ClueCreateRequest(
                "puzzle123",
                "Across",
                1,
                "",
                "APPLE"
        );

        assertFalse(validator.validate(dto).isEmpty());
    }

}
