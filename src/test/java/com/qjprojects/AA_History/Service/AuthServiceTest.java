package com.qjprojects.AA_History.Service;

import com.qjprojects.AA_History.DTO.AuthResponse;
import com.qjprojects.AA_History.DTO.LoginRequest;
import com.qjprojects.AA_History.DTO.RegisterRequest;
import com.qjprojects.AA_History.Entity.AppUser;
import com.qjprojects.AA_History.Repository.AppUserRepository;
import com.qjprojects.AA_History.Security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest(
                "testuser",
                "test@example.com",
                "password123",
                null,
                null,
                null
        );

        loginRequest = new LoginRequest(
                "test@example.com",
                "password123"
        );
    }

    @Test
    void registerReturnsTokenAndUser() {
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$hashedpassword");
        when(userRepository.save(any(AppUser.class))).thenAnswer(i -> i.getArgument(0));
        when(jwtService.generateToken(any(AppUser.class))).thenReturn("mock.jwt.token");

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("mock.jwt.token", response.getToken());
        assertNotNull(response.getUser());
        assertEquals("testuser", response.getUser().getUsername());

        verify(userRepository, times(1)).save(any(AppUser.class));
        verify(jwtService, times(1)).generateToken(any(AppUser.class));
    }

    @Test
    void registerEncodesPassword() {
        when(passwordEncoder.encode("password123")).thenReturn("$2a$hashedpassword");
        when(userRepository.save(any(AppUser.class))).thenAnswer(i -> i.getArgument(0));
        when(jwtService.generateToken(any(AppUser.class))).thenReturn("mock.jwt.token");

        authService.register(registerRequest);

        verify(passwordEncoder, times(1)).encode("password123");
    }

    @Test
    void loginSuccessReturnsToken() {
        AppUser user = new AppUser("testuser", "test@example.com", "$2a$hashedpassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "$2a$hashedpassword")).thenReturn(true);
        when(jwtService.generateToken(any(AppUser.class))).thenReturn("mock.jwt.token");

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("mock.jwt.token", response.getToken());
        verify(jwtService, times(1)).generateToken(user);
    }

    @Test
    void loginWithUnknownEmailThrows() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                authService.login(loginRequest));

        assertEquals("Invalid credentials", ex.getMessage());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void loginWithWrongPasswordThrows() {
        AppUser user = new AppUser("testuser", "test@example.com", "$2a$hashedpassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "$2a$hashedpassword")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                authService.login(loginRequest));

        assertEquals("Invalid credentials", ex.getMessage());
        verify(jwtService, never()).generateToken(any());
    }
}