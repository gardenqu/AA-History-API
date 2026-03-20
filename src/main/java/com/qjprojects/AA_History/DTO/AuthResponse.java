package com.qjprojects.AA_History.DTO;

import java.util.Set;

public class AuthResponse {
    private String token;
    private String userId;
    private String username;
    private Set<String> roles;
    private boolean isProfileComplete;

    public AuthResponse(String token, String userId, String username,
                        Set<String> roles, boolean isProfileComplete) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.roles = roles;
        this.isProfileComplete = isProfileComplete;
    }

    public String getToken() { return token; }
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public Set<String> getRoles() { return roles; }
    public boolean isProfileComplete() { return isProfileComplete; }
}