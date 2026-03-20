package com.qjprojects.AA_History.Service;

import com.qjprojects.AA_History.Entity.AppUser;
import com.qjprojects.AA_History.Entity.SecurityLog;
import com.qjprojects.AA_History.Repository.SecurityLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SecurityLogServiceTest {

    @Mock
    private SecurityLogRepository securityLogRepository;

    @InjectMocks
    private SecurityLogService securityLogService;

    private AppUser mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new AppUser("testuser", "test@example.com", "hashedpassword");
    }

    @Test
    void logWithoutDetailsSavesCorrectly() {
        securityLogService.log(mockUser, "USER_BANNED");

        ArgumentCaptor<SecurityLog> captor = ArgumentCaptor.forClass(SecurityLog.class);
        verify(securityLogRepository).save(captor.capture());

        SecurityLog saved = captor.getValue();
        assertEquals(mockUser, saved.getUser());
        assertEquals("USER_BANNED", saved.getEventType());
        assertNotNull(saved.getCreatedAt());
        assertNull(saved.getDetails());
    }

    @Test
    void logWithDetailsSavesCorrectly() {
        securityLogService.log(mockUser, "ROLES_ASSIGNED", "ADMIN, USER");

        ArgumentCaptor<SecurityLog> captor = ArgumentCaptor.forClass(SecurityLog.class);
        verify(securityLogRepository).save(captor.capture());

        SecurityLog saved = captor.getValue();
        assertEquals(mockUser, saved.getUser());
        assertEquals("ROLES_ASSIGNED", saved.getEventType());
        assertEquals("ADMIN, USER", saved.getDetails());
    }

    @Test
    void getLogsForUserCallsRepository() {
        String userId = "test-user-id";
        when(securityLogRepository.findByUserIdOrderByCreatedAtDesc(userId))
                .thenReturn(List.of());

        List<SecurityLog> logs = securityLogService.getLogsForUser(userId);

        verify(securityLogRepository).findByUserIdOrderByCreatedAtDesc(userId);
        assertNotNull(logs);
    }

    @Test
    void getLogsForUserReturnsLogs() {
        String userId = "test-user-id";
        SecurityLog log1 = new SecurityLog(mockUser, "USER_BANNED", null, null);
        SecurityLog log2 = new SecurityLog(mockUser, "USER_UNBANNED", null, null);

        when(securityLogRepository.findByUserIdOrderByCreatedAtDesc(userId))
                .thenReturn(List.of(log1, log2));

        List<SecurityLog> logs = securityLogService.getLogsForUser(userId);

        assertEquals(2, logs.size());
        assertEquals("USER_BANNED", logs.get(0).getEventType());
        assertEquals("USER_UNBANNED", logs.get(1).getEventType());
    }
}