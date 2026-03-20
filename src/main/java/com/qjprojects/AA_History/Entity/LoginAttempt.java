package com.qjprojects.AA_History.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "login_attempt")
public class LoginAttempt {

    @Id
    @Column(name = "attempt_id", length = 36, nullable = false, updatable = false)
    private String attemptID = UUID.randomUUID().toString();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;

    @Column(length = 120)
    private String email;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    private boolean success = false;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "attempted_at", nullable = false)
    private LocalDateTime attemptedAt;

    public LoginAttempt() {
        // JPA default constructor
    }

    public LoginAttempt(AppUser user,
                        boolean success,
                        String ipAddress,
                        LocalDateTime timestamp) {

        this.user = user;
        this.success = success;
        this.ipAddress = ipAddress;
        this.attemptedAt = timestamp;

        // If user is provided, store their email for analytics
        if (user != null) {
            this.email = user.getEmail();
        }
    }

    // Optional: constructor for failed attempts without a user
    public LoginAttempt(String email,
                        boolean success,
                        String ipAddress,
                        String failureReason,
                        LocalDateTime timestamp) {

        this.email = email;
        this.success = success;
        this.ipAddress = ipAddress;
        this.failureReason = failureReason;
        this.attemptedAt = timestamp;
    }


    //getter and setter

    public String getAttemptID() {
        return attemptID;
    }

    public void setAttemptID(String attemptID) {
        this.attemptID = attemptID;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public LocalDateTime getAttemptedAt() {
        return attemptedAt;
    }

    public void setAttemptedAt(LocalDateTime attemptedAt) {
        this.attemptedAt = attemptedAt;
    }

}