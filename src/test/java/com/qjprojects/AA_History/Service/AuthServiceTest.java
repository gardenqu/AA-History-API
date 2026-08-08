package com.qjprojects.AA_History.Service;

import com.qjprojects.AA_History.DTO.AuthResponse;
import com.qjprojects.AA_History.DTO.LoginRequest;
import com.qjprojects.AA_History.DTO.RegisterRequest;
import com.qjprojects.AA_History.Entity.AppUser;
import com.qjprojects.AA_History.Entity.LoginAttempt;
import com.qjprojects.AA_History.Entity.Role;
import com.qjprojects.AA_History.Exception.AuthException;
import com.qjprojects.AA_History.Repository.AppUserRepository;
import com.qjprojects.AA_History.Repository.LoginAttemptRepository;
import com.qjprojects.AA_History.Repository.RoleRepository;
import com.qjprojects.AA_History.Security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock private AppUserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private RoleRepository roleRepository;
    @Mock private LoginAttemptRepository loginAttemptRepository;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private HttpServletRequest httpRequest;

    @InjectMocks private AuthService authService;

    private AppUser mockUser;
    private Role userRole;

    @BeforeEach
    void setUp() {
        userRole = new Role();
        userRole.setName("USER");

        mockUser = new AppUser("testuser", "test@example.com", "hashedpassword");
        mockUser.getRoles().add(userRole);
    }

    @Test
    void registerCreatesUserAndReturnsToken() {
        RegisterRequest request = new RegisterRequest(
                "testuser",
                "test@example.com",
                "password123",
                "Test User",
                null,
                null
        );

        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));
        when(userRepository.save(any(AppUser.class))).thenReturn(mockUser);
        when(jwtService.generateToken(any(AppUser.class))).thenReturn("jwt-token");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        verify(userRepository).save(any(AppUser.class));
    }

    @Test
    void registerThrowsWhenUserRoleNotFound() {
        RegisterRequest request = new RegisterRequest(
                "testuser",
                "test@example.com",
                "password123",
                "Test User",
                null,
                null
        );
        when(roleRepository.findByName("USER")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.register(request));
    }

    @Test
    void loginSuccessReturnsTokenAndLogsAttempt() {
        LoginRequest request = new LoginRequest("test@example.com", "password123");
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUser);
        when(jwtService.generateToken(any(AppUser.class))).thenReturn("jwt-token");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());

        ArgumentCaptor<LoginAttempt> captor = ArgumentCaptor.forClass(LoginAttempt.class);
        verify(loginAttemptRepository).save(captor.capture());
        assertTrue(captor.getValue().isSuccess());
    }

    @Test
    void loginFailsWhenUserNotFoundAndLogsAttempt() {
        LoginRequest request = new LoginRequest("unknown@example.com", "password123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThrows(AuthException.class, () -> authService.login(request));

        ArgumentCaptor<LoginAttempt> captor = ArgumentCaptor.forClass(LoginAttempt.class);
        verify(loginAttemptRepository).save(captor.capture());
        assertFalse(captor.getValue().isSuccess());
        assertEquals("org.springframework.security.authentication.BadCredentialsException: Bad credentials", captor.getValue().getFailureReason());
    }

    @Test
    void loginFailsWhenWrongPasswordAndLogsAttempt() {
        LoginRequest request = new LoginRequest("test@example.com", "wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));

        assertThrows(AuthException.class, () -> authService.login(request));

        ArgumentCaptor<LoginAttempt> captor = ArgumentCaptor.forClass(LoginAttempt.class);
        verify(loginAttemptRepository).save(captor.capture());
        assertFalse(captor.getValue().isSuccess());
        assertEquals("org.springframework.security.authentication.BadCredentialsException: Bad credentials", captor.getValue().getFailureReason());
    }

    @Test
    void loginResponseContainsCorrectFields() {
        LoginRequest request = new LoginRequest("test@example.com", "password123");
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUser);
        when(jwtService.generateToken(any(AppUser.class))).thenReturn("jwt-token");

        AuthResponse response = authService.login(request);

        assertEquals("jwt-token", response.getToken());
        assertEquals("testuser", response.getUsername());
        assertTrue(response.getRoles().contains("USER"));
    }
}