package com.qjprojects.AA_History.DTO;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class PasswordChangeRequestTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void constructorSetsFieldsCorrectly() {
        PasswordChangeRequest dto = new PasswordChangeRequest("newPassword123", "token123");

        assertEquals("token123", dto.getToken());
        assertEquals("newPassword123", dto.getNewPassword());
    }

    @Test
    void validationFailsForBlankToken() {
        PasswordChangeRequest dto = new PasswordChangeRequest("", "newPassword123");

        assertFalse(validator.validate(dto).isEmpty());
    }

}
