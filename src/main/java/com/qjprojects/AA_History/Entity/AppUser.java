package com.qjprojects.AA_History.Entity;

import jakarta.persistence.*;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name="app_user")
public class AppUser implements UserDetails, CredentialsContainer {

    @Id
    @Column(name = "user_id", length = 36, nullable = false, updatable = false)
    private String id = UUID.randomUUID().toString();

    @Column(nullable = false)
    private boolean isOauth = false;

    // Identity
    @Column(length = 80, nullable = false, unique = true)
    private String username;

    @Column(length = 120, unique = true, nullable = false)
    private String email;

    @Column(length = 100)
    private String name;

    private LocalDate birthDate;

    @Column(length = 20)
    private String phoneNumber;

    // OAuth IDs
    @Column(unique = true)
    private String googleID;

    @Column(unique = true)
    private String appleID;



    @Column(length = 128)
    private String passwordHash;

    private LocalDateTime passwordChangedAt;

    private Boolean isActive = true;
    private Boolean isVerified = false;
    private Boolean isTwoFactorEnabled = false;

    private LocalDateTime lastLogin;

    @Column(length = 50)
    private String lastLoginIP;

    @Column(name = "is_profile_complete", nullable = false)
    private Boolean isProfileComplete = false;

    public Boolean getProfileComplete() { return isProfileComplete; }
    public void setProfileComplete(Boolean profileComplete) {
        isProfileComplete = profileComplete; }

    // Brute-force protection
    @Column(nullable = false)
    private Integer failedLoginAttempts = 0;

    private LocalDateTime lastFailedLogin;

    @Column(nullable = false)
    private Boolean isBanned = false;

    @Column
    private LocalDateTime lockedUntil;

    // Roles
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // Security logs
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<SecurityLog> securityLogs = new ArrayList<>();

    // Password reset tokens
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PasswordResetToken> passwordResetTokens = new ArrayList<>();

    // Timestamps
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();


    // ---------------------------------------------------------
    // ⭐ REQUIRED BY JPA + YOUR TESTS
    // ---------------------------------------------------------
    public AppUser() {  // ← changed from protected to public
    }

    // ---------------------------------------------------------
    // CONVENIENCE CONSTRUCTOR FOR REAL USER CREATION
    // ---------------------------------------------------------
    public AppUser(String username, String email, String passwordHash) {
        this.username = username;
        this.email = email;

        if (passwordHash != null) {
            this.passwordHash = passwordHash;
            this.passwordChangedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Crossword-specific stats
    @Column(name = "puzzles_solved", nullable = false)
    private Integer puzzlesSolved = 0;

    @Column(name = "total_time_spent", nullable = false)
    private Integer totalTimeSpent = 0;

    @Column(name = "current_streak", nullable = false)
    private Integer currentStreak = 0;

    @Column(name = "longest_streak", nullable = false)
    private Integer longestStreak = 0;

    // Solve history
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<PuzzleSolve> solveHistory = new HashSet<>();



    // Helper functions
    // Called from AuthService with raw password
    public void setPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        this.passwordHash = passwordEncoder.encode(rawPassword);
        this.passwordChangedAt = LocalDateTime.now();
    }

    public boolean hasPermission(String resource, String action) {
        for (Role role : roles) {
            for (Permission perm : role.getPermissions()) {
                if (perm.getResource().equals(resource) && perm.getAction().equals(action))
                    return true;
            }
        }
        return false;
    }

    public boolean hasRole(String roleName) {
        return roles.stream().anyMatch(role -> role.getName().equals(roleName));
    }

    @Override
    public String toString() {
        return "<AppUser " + username + ">";
    }

    // Getters and setters

    public List<PasswordResetToken> getPasswordResetTokens() {
        return passwordResetTokens;
    }

    public void setPasswordResetTokens(List<PasswordResetToken> passwordResetTokens) {
        this.passwordResetTokens = passwordResetTokens;
    }

    public Integer getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(Integer failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public LocalDateTime getLastFailedLogin() {
        return lastFailedLogin;
    }

    public void setLastFailedLogin(LocalDateTime lastFailedLogin) {
        this.lastFailedLogin = lastFailedLogin;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public boolean isOauth() { return isOauth; }
    public void setOauth(boolean oauth) { isOauth = oauth; }

    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getGoogleID() { return googleID; }
    public void setGoogleID(String googleID) { this.googleID = googleID; }

    public String getAppleID() { return appleID; }
    public void setAppleID(String appleID) { this.appleID = appleID; }

    public String getPasswordHash() { return passwordHash; }

    // Called internally or when hash is already encoded
    public void setPasswordHash(String hash) {
        this.passwordHash = hash;
        this.passwordChangedAt = LocalDateTime.now();
    }

    public LocalDateTime getPasswordChangedAt() { return passwordChangedAt; }
    public void setPasswordChangedAt(LocalDateTime passwordChangedAt) { this.passwordChangedAt = passwordChangedAt; }

    public Boolean getActive() { return isActive; }
    public void setActive(Boolean active) { isActive = active; }

    public Boolean getVerified() { return isVerified; }
    public void setVerified(Boolean verified) { isVerified = verified; }

    public Boolean getTwoFactorEnabled() { return isTwoFactorEnabled; }
    public void setTwoFactorEnabled(Boolean twoFactorEnabled) { isTwoFactorEnabled = twoFactorEnabled; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    public String getLastLoginIP() { return lastLoginIP; }
    public void setLastLoginIP(String lastLoginIP) { this.lastLoginIP = lastLoginIP; }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    public List<SecurityLog> getSecurityLogs() { return securityLogs; }
    public void setSecurityLogs(List<SecurityLog> securityLogs) { this.securityLogs = securityLogs; }

    public Integer getPuzzlesSolved() { return puzzlesSolved; }
    public void setPuzzlesSolved(Integer puzzlesSolved) { this.puzzlesSolved = puzzlesSolved; }

    public Integer getTotalTimeSpent() { return totalTimeSpent; }
    public void setTotalTimeSpent(Integer totalTimeSpent) { this.totalTimeSpent = totalTimeSpent; }

    public Integer getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(Integer currentStreak) { this.currentStreak = currentStreak; }

    public Integer getLongestStreak() { return longestStreak; }
    public void setLongestStreak(Integer longestStreak) { this.longestStreak = longestStreak; }

    public Set<PuzzleSolve> getSolveHistory() { return solveHistory; }
    public void setSolveHistory(Set<PuzzleSolve> solveHistory) { this.solveHistory = solveHistory; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }


    public Boolean getBanned() { return isBanned; }
    public void setBanned(Boolean banned) { isBanned = banned; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public @NonNull String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() {
        return lockedUntil == null || LocalDateTime.now().isAfter(lockedUntil);
    }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return isActive; }

    @Override
    public void eraseCredentials() {
        this.passwordHash = null;
    }
}