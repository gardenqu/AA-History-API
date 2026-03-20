package com.qjprojects.AA_History.Service;

import com.qjprojects.AA_History.Entity.AppUser;
import com.qjprojects.AA_History.Repository.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private AppUserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private AppUser user;

    @BeforeEach
    void setUp() {
        user = new AppUser();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPasswordHash("$2a$10$fakehashfortesting");
    }

    @Test
    void getByIdReturnsUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        AppUser result = userService.getById(user.getId());

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void getByIdThrowsWhenNotFound() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userService.getById("nonexistent-id"));

        assertEquals("User not found", ex.getMessage());
        verify(userRepository, times(1)).findById("nonexistent-id");
    }

    @Test
    void getByEmailReturnsUser() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        AppUser result = userService.getByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void getByEmailThrowsWhenNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userService.getByEmail("ghost@example.com"));

        assertEquals("User not found", ex.getMessage());
        verify(userRepository, times(1)).findByEmail("ghost@example.com");
    }

    @Test
    void saveReturnsUser() {
        when(userRepository.save(any(AppUser.class))).thenReturn(user);

        AppUser result = userService.save(user);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).save(user);
    }
}