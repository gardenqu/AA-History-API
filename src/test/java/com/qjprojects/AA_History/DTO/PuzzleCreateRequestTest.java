package com.qjprojects.AA_History.DTO;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class PuzzleCreateRequestTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void constructorSetsFieldsCorrectly() {
        Map<String, Object> grid = Map.of("0,0", "A");

        PuzzleCreateRequest dto = new PuzzleCreateRequest(
                "Daily Mini",
                "Easy",
                "Author",
                5,
                5,
                grid,
                "Black History"
        );

        assertEquals("Daily Mini", dto.getTitle());
        assertEquals("Easy", dto.getDifficulty());
        assertEquals("Author", dto.getAuthor());
        assertEquals(5, dto.getWidth());
        assertEquals(5, dto.getHeight());
        assertEquals(grid, dto.getGrid());
        assertEquals("Black History", dto.getCategory());
    }

    @Test
    void validationFailsForBlankTitle() {
        PuzzleCreateRequest dto = new PuzzleCreateRequest(
                "",
                "Easy",
                "Author",
                5,
                5,
                Map.of("0,0", "A"),
                "Black History"
        );

        assertFalse(validator.validate(dto).isEmpty());
    }


}
