package com.qjprojects.AA_History.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final LoginAttemptRepository loginAttemptRepository;
    private final AuthenticationManager authenticationManager;
    private final HttpServletRequest httpRequest;



    private static final int MAX_FAILED_LOGIN_ATTEMPTS = 5;
    private static final int LOCKOUT_MINUTES = 15;


    public AuthService(
            AppUserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RoleRepository roleRepository,
            LoginAttemptRepository loginAttemptRepository,
            AuthenticationManager authenticationManager, HttpServletRequest httpRequest
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.roleRepository = roleRepository;
        this.loginAttemptRepository = loginAttemptRepository;
        this.authenticationManager = authenticationManager;
        this.httpRequest = httpRequest;
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
            if (user == null) {
                throw new BadCredentialsException("User not Found");
            }

            user.setFailedLoginAttempts(0);
            user.setLastFailedLogin(null);
            user.setLockedUntil(null);
            user.setLastLogin(LocalDateTime.now());
            user.setLastLoginIP(httpRequest.getRemoteAddr());
            userRepository.save(user);

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
            handleFailedLogin(request.getEmail());
            loginAttemptRepository.save(new LoginAttempt(
                    request.getEmail(),
                    false,
                    null,
                    e.toString(),
                    LocalDateTime.now()
            ));

            throw new AuthException("Invalid credentials");
        }
    }

    @Value("${google.client-id}")
    private String googleClientId;

    public AuthResponse googleLogin(String idToken) {
        try {
            // Verify the token with Google
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    new GsonFactory()
            )
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken googleIdToken = verifier.verify(idToken);

            if (googleIdToken == null) {
                throw new AuthException("Invalid Google token");
            }

            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            String googleId = payload.getSubject();
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            // Find existing user or create new one
            AppUser user = userRepository.findByGoogleID(googleId)
                    .orElseGet(() -> userRepository.findByEmail(email)
                            .orElseGet(() -> createGoogleUser(googleId, email, name)));

            // If user exists but doesn't have googleID linked yet
            if (user.getGoogleID() == null) {
                user.setGoogleID(googleId);
                user.setOauth(true);
                userRepository.save(user);
            }

            // Log successful attempt
            loginAttemptRepository.save(new LoginAttempt(
                    user, true, null, LocalDateTime.now()
            ));

            String token = jwtService.generateToken(user);
            return buildAuthResponse(token, user);

        } catch (Exception e) {
            loginAttemptRepository.save(new LoginAttempt(
                    "unknown", false, null, "Google OAuth failed", LocalDateTime.now()
            ));
            throw new AuthException("Google authentication failed");
        }
    }

    private AppUser createGoogleUser(String googleId, String email, String name) {
        AppUser user = new AppUser();
        user.setUsername(email.split("@")[0] + "_" + UUID.randomUUID().toString().substring(0, 8));
        user.setEmail(email);
        user.setName(name);
        user.setGoogleID(googleId);
        user.setOauth(true);
        user.setVerified(true); // Google already verified the email

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("USER role not found"));
        user.getRoles().add(userRole);

        return userRepository.save(user);
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

    private void handleFailedLogin(String email) {

        AppUser user = userRepository
                .findByEmail(email)
                .orElse(null);

        // Don't reveal whether the email exists
        if (user == null) {
            return;
        }

        int attempts = user.getFailedLoginAttempts() + 1;

        user.setFailedLoginAttempts(attempts);
        user.setLastFailedLogin(LocalDateTime.now());

        if (attempts >= MAX_FAILED_LOGIN_ATTEMPTS) {
            user.setLockedUntil(
                    LocalDateTime.now().plusMinutes(LOCKOUT_MINUTES)
            );
        }

        userRepository.save(user);
    }
}