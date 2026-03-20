package com.qjprojects.AA_History.DTO;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class LoginRequestTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void constructorSetsFieldsCorrectly() {
        LoginRequest dto = new LoginRequest("user@example.com", "password123");

        assertEquals("user@example.com", dto.getEmail());
        assertEquals("password123", dto.getPassword());
    }

    @Test
    void validationFailsForInvalidEmail() {
        LoginRequest dto = new LoginRequest("not-an-email", "password123");

        assertFalse(validator.validate(dto).isEmpty());
    }

}
