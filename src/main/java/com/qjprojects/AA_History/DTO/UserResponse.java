package com.qjprojects.AA_History.DTO;

import java.util.Set;

public class UserResponse {
    private String id;
    private String username;
    private String email;
    private String name;
    private String phoneNumber;
    private String birthDate;
    private boolean isVerified;
    private boolean isActive;
    private boolean isBanned;
    private boolean isProfileComplete;
    private Set<String> roles;

    public UserResponse(String id, String username, String email, String name,
                        String phoneNumber, String birthDate, boolean isVerified,
                        boolean isActive, boolean isBanned, boolean isProfileComplete,
                        Set<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.isVerified = isVerified;
        this.isActive = isActive;
        this.isBanned = isBanned;
        this.isProfileComplete = isProfileComplete;
        this.roles = roles;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getBirthDate() { return birthDate; }
    public boolean isVerified() { return isVerified; }
    public boolean isActive() { return isActive; }
    public boolean isBanned() { return isBanned; }
    public boolean isProfileComplete() { return isProfileComplete; }
    public Set<String> getRoles() { return roles; }
}