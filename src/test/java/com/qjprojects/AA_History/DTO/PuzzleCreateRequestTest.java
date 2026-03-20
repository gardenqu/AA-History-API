package com.qjprojects.AA_History.DTO;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PuzzleCreateRequestTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void constructorSetsFieldsCorrectly() {
        PuzzleCreateRequest dto = new PuzzleCreateRequest(
                "Daily Mini",
                "Easy",
                "Author",
                "Black History"
        );

        assertEquals("Daily Mini", dto.getTitle());
        assertEquals("Easy", dto.getDifficulty());
        assertEquals("Author", dto.getAuthor());
        assertEquals("Black History", dto.getCategory());
    }

    @Test
    void validationFailsForBlankTitle() {
        PuzzleCreateRequest dto = new PuzzleCreateRequest(
                "",
                "Easy",
                "Author",
                "Black History"
        );

        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void validationFailsForBlankDifficulty() {
        PuzzleCreateRequest dto = new PuzzleCreateRequest(
                "Daily Mini",
                "",
                "Author",
                "Black History"
        );

        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void validationFailsForBlankCategory() {
        PuzzleCreateRequest dto = new PuzzleCreateRequest(
                "Daily Mini",
                "Easy",
                "Author",
                ""
        );

        assertFalse(validator.validate(dto).isEmpty());
    }
}