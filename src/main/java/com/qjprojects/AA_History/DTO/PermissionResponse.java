package com.qjprojects.AA_History.DTO;

public class PermissionResponse {
    private String permissionId;
    private String name;

    private String description;
    private String resource;
    private String action;

    public PermissionResponse(String permissionId, String name, String description, String resource, String action) {
        this.permissionId = permissionId;
        this.name = name;
        this.description = description;
        this.resource = resource;
        this.action = action;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getResource() {
        return resource;
    }

    public String getAction() {
        return action;
    }
}
