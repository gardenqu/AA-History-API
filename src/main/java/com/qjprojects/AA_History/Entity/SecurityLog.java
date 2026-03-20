package com.qjprojects.AA_History.Entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="security_log")
public class SecurityLog {


    @Id
    @Column( name="log_id",length=36, nullable = false, updatable = false)
    private String logId= UUID.randomUUID().toString();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private  AppUser user;

    @Column(name = "event_type",length = 100, nullable = false)
    private String eventType;

    @Column(name="ip_address",length = 50)
    private String ipAddress;

    @Column(name="user_agent")
    private String userAgent;

    private String details;

    @Column(name = "created_at")
    private LocalDateTime createdAt= LocalDateTime.now();

    public SecurityLog(){}

    public SecurityLog(AppUser user,
                       String eventType,
                       LocalDateTime createdAt,
                       String ipAddress) {

        this.user = user;
        this.eventType = eventType;
        this.createdAt = createdAt;
        this.ipAddress = ipAddress;
    }


    //getters and setters
    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
