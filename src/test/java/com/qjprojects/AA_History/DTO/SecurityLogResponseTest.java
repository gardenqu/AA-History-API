package com.qjprojects.AA_History.DTO;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;



public class SecurityLogResponseTest {
    @Test
    void constructorSetsFieldsCorrectly() {
        LocalDateTime now = LocalDateTime.now();

        SecurityLogResponse dto = new SecurityLogResponse(
                "log123",
                "user123",
                "LOGIN_SUCCESS",
                "127.0.0.1",
                "Mozilla",
                "Successful login",
                now
        );

        assertEquals("log123", dto.getLogId());
        assertEquals("user123", dto.getUserId());
        assertEquals("LOGIN_SUCCESS", dto.getEventType());
        assertEquals("127.0.0.1", dto.getIpAddress());
        assertEquals("Mozilla", dto.getUserAgent());
        assertEquals("Successful login", dto.getDetails());
        assertEquals(now, dto.getCreatedAt());
    }

}
