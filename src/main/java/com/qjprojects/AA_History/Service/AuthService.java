package com.qjprojects.AA_History.Service;

import com.qjprojects.AA_History.DTO.AuthResponse;
import com.qjprojects.AA_History.DTO.LoginRequest;
import com.qjprojects.AA_History.DTO.RegisterRequest;
import com.qjprojects.AA_History.Entity.AppUser;
import com.qjprojects.AA_History.Entity.LoginAttempt;
import com.qjprojects.AA_History.Entity.Role;
import com.qjprojects.AA_History.Repository.AppUserRepository;
import com.qjprojects.AA_History.Repository.LoginAttemptRepository;
import com.qjprojects.AA_History.Repository.RoleRepository;
import com.qjprojects.AA_History.Exception.AuthException;
import com.qjprojects.AA_History.Security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final LoginAttemptRepository loginAttemptRepository;

    public AuthService(
            AppUserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RoleRepository roleRepository,
            LoginAttemptRepository loginAttemptRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.roleRepository = roleRepository;
        this.loginAttemptRepository = loginAttemptRepository;
    }

    public AuthResponse register(RegisterRequest request) {
        AppUser user = new AppUser(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
        );

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("USER role not found"));
        user.getRoles().add(userRole);

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return buildAuthResponse(token, user);
    }

    public AuthResponse login(LoginRequest request) {
        // Try to find the user
        AppUser user = userRepository.findByEmail(request.getEmail()).orElse(null);

        // User not found — log failed attempt with no user attached
        if (user == null) {
            loginAttemptRepository.save(new LoginAttempt(
                    request.getEmail(),
                    false,
                    null,
                    "User not found",
                    LocalDateTime.now()
            ));
            throw new AuthException("Invalid credentials");
        }

        // Wrong password — log failed attempt with user attached
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            loginAttemptRepository.save(new LoginAttempt(
                    request.getEmail(),
                    false,
                    null,
                    "Invalid password",
                    LocalDateTime.now()
            ));
            throw new AuthException("Invalid credentials");
        }

        // Success — log successful attempt
        loginAttemptRepository.save(new LoginAttempt(
                user,
                true,
                null,
                LocalDateTime.now()
        ));

        String token = jwtService.generateToken(user);

        return buildAuthResponse(token, user);
    }

    private AuthResponse buildAuthResponse(String token, AppUser user) {
        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new AuthResponse(
                token,
                user.getId(),
                user.getUsername(),
                roles,
                user.getProfileComplete()
        );
    }
}