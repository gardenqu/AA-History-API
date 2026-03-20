package com.qjprojects.AA_History.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="password_reset_token")
public class PasswordResetToken {

    @Id
    @Column(name = "token_id", length = 36, nullable = false, updatable = false)
    private String id = UUID.randomUUID().toString();

    @Column(nullable = false, unique = true, length = 36)
    private String token = UUID.randomUUID().toString();  // ← added this

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    private LocalDateTime expiresAt;

    public PasswordResetToken() {}

    public PasswordResetToken(AppUser user, LocalDateTime expiresAt) {
        this.user = user;
        this.expiresAt = expiresAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getToken() { return token; }          // ← added getter
    public void setToken(String token) { this.token = token; }  // ← added setter

    public AppUser getUser() { return user; }
    public void setUser(AppUser user) { this.user = user; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
}