package com.qjprojects.AA_History.DTO;

import java.time.LocalDateTime;

public class LoginAttemptResponse {
    private String attemptId;
    private String email;
    private boolean success;
    private String ipAddress;
    private String userAgent;
    private String failureReason;
    private LocalDateTime attemptedAt;

    public LoginAttemptResponse(String attemptId, String email, boolean success, String ipAddress, String userAgent, String failureReason, LocalDateTime attemptedAt) {
        this.attemptId = attemptId;
        this.email = email;
        this.success = success;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.failureReason = failureReason;
        this.attemptedAt = attemptedAt;
    }

    public String getAttemptId() {
        return attemptId;
    }

    public String getEmail() {
        return email;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public LocalDateTime getAttemptedAt() {
        return attemptedAt;
    }
}
