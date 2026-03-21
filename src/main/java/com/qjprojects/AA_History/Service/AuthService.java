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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private final AuthenticationManager authenticationManager;

    public AuthService(
            AppUserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RoleRepository roleRepository,
            LoginAttemptRepository loginAttemptRepository,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.roleRepository = roleRepository;
        this.loginAttemptRepository = loginAttemptRepository;
        this.authenticationManager = authenticationManager;
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
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            AppUser user = (AppUser) authentication.getPrincipal();

            // Log successful attempt
            loginAttemptRepository.save(new LoginAttempt(
                    user,
                    true,
                    null,
                    LocalDateTime.now()
            ));

            String token = jwtService.generateToken(user);
            return buildAuthResponse(token, user);

        } catch (BadCredentialsException e) {
            // Log failed attempt
            loginAttemptRepository.save(new LoginAttempt(
                    request.getEmail(),
                    false,
                    null,
                    "Invalid credentials",
                    LocalDateTime.now()
            ));
            throw new AuthException("Invalid credentials");
        }
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