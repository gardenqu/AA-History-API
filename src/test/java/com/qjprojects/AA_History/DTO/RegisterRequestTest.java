package com.qjprojects.AA_History.DTO;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterRequestTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void constructorSetsFieldsCorrectly() {
        RegisterRequest dto = new RegisterRequest(
                "user123",
                "user@example.com",
                "password123",
                "Test User",
                LocalDate.of(2000, 1, 1),
                "555-5555"
        );

        assertEquals("user123", dto.getUsername());
        assertEquals("user@example.com", dto.getEmail());
        assertEquals("password123", dto.getPassword());
        assertEquals("Test User", dto.getName());
        assertEquals(LocalDate.of(2000, 1, 1), dto.getBirthDate());
        assertEquals("555-5555", dto.getPhoneNumber());
    }

    @Test
    void validationFailsForBlankUsername() {
        RegisterRequest dto = new RegisterRequest(
                "",
                "user@example.com",
                "password123",
                "Test User",
                LocalDate.of(2000, 1, 1),
                "555-5555"
        );

        assertFalse(validator.validate(dto).isEmpty());
    }


}
