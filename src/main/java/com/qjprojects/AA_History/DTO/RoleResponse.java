package com.qjprojects.AA_History.DTO;

import java.util.Set;

public class RoleResponse {
    private String roleId;
    private String name;
    private String description;
    private Set<String> permissions;

    public RoleResponse(String roleId, String name, String description, Set<String> permissions) {
        this.roleId = roleId;
        this.name = name;
        this.description = description;
        this.permissions = permissions;
    }

    public String getRoleId() {
        return roleId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<String> getPermissions() {
        return permissions;
    }
}
