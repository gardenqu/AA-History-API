package com.qjprojects.AA_History.DTO;

import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordResetRequestTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();



    @Test
    void constructorSetsFieldsCorrectly() {
        PasswordResetRequest dto = new PasswordResetRequest("user@example.com");

        assertEquals("user@example.com", dto.getEmail());
    }

    @Test
    void validationFailsForInvalidEmail() {
        PasswordResetRequest dto = new PasswordResetRequest("not-an-email");

        assertFalse(validator.validate(dto).isEmpty());
    }

}
