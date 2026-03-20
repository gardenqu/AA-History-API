package com.qjprojects.AA_History.Service;

import com.qjprojects.AA_History.Entity.AppUser;
import com.qjprojects.AA_History.Entity.SecurityLog;
import com.qjprojects.AA_History.Repository.SecurityLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SecurityLogService {

    private final SecurityLogRepository securityLogRepository;

    public SecurityLogService(SecurityLogRepository securityLogRepository) {
        this.securityLogRepository = securityLogRepository;
    }

    public void log(AppUser user, String eventType) {
        SecurityLog log = new SecurityLog(user, eventType, LocalDateTime.now(), null);
        securityLogRepository.save(log);
    }

    public void log(AppUser user, String eventType, String details) {
        SecurityLog log = new SecurityLog(user, eventType, LocalDateTime.now(), null);
        log.setDetails(details);
        securityLogRepository.save(log);
    }

    public List<SecurityLog> getLogsForUser(String userId) {
        return securityLogRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}