package com.qjprojects.AA_History.DTO;

import java.time.LocalDateTime;

public class SecurityLogResponse {
    private String logId;
    private String userId;
    private String eventType;
    private String ipAddress;
    private String userAgent;

    private String details;
    private LocalDateTime createdAt;


    //constructor
    public SecurityLogResponse(String logId, String userId, String eventType, String ipAddress, String userAgent, String details, LocalDateTime createdAt) {
        this.logId = logId;
        this.userId = userId;
        this.eventType = eventType;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.details = details;
        this.createdAt = createdAt;
    }

    public String getLogId() {
        return logId;
    }

    public String getUserId() {
        return userId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getDetails() {
        return details;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
