package com.qjprojects.AA_History.DTO;

import jakarta.validation.constraints.NotBlank;

public class GoogleOAuthRequest {

    @NotBlank
    private String idToken;

    public GoogleOAuthRequest(String idToken) {
        this.idToken = idToken;
    }

    public String getIdToken() { return idToken; }
}