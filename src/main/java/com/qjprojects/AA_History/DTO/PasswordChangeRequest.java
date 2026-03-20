package com.qjprojects.AA_History.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PasswordChangeRequest {
    @NotBlank
    private String token;

    @Size(min = 8)
    private String newPassword;

    public PasswordChangeRequest(String newPassword, String token) {
        this.newPassword = newPassword;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
