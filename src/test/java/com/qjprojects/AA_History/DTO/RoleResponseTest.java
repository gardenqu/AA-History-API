package com.qjprojects.AA_History.DTO;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoleResponseTest {
    @Test
    void constructorSetsFieldsCorrectly() {
        Set<String> permissions = Set.of("READ", "WRITE");

        RoleResponse dto = new RoleResponse(
                "role123",
                "ADMIN",
                "Administrator role",
                permissions
        );

        assertEquals("role123", dto.getRoleId());
        assertEquals("ADMIN", dto.getName());
        assertEquals("Administrator role", dto.getDescription());
        assertEquals(permissions, dto.getPermissions());
    }

}
