package com.qjprojects.AA_History.Service;

import com.qjprojects.AA_History.Entity.AppUser;
import com.qjprojects.AA_History.Entity.PasswordResetToken;
import com.qjprojects.AA_History.Repository.AppUserRepository;
import com.qjprojects.AA_History.Repository.PasswordResetTokenRepository;
import com.qjprojects.AA_History.Exception.AuthException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PasswordResetService {

    private final AppUserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Value("${app.password-reset.expiration-hours}")
    private int expirationHours;

    public PasswordResetService(AppUserRepository userRepository,
                                PasswordResetTokenRepository tokenRepository,
                                JavaMailSender mailSender,
                                PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void requestReset(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            tokenRepository.deleteByUserId(user.getId());

            PasswordResetToken resetToken = new PasswordResetToken(
                    user,
                    LocalDateTime.now().plusHours(expirationHours)
            );

            tokenRepository.save(resetToken);
            sendResetEmail(user.getEmail(), resetToken.getToken());
        });
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new AuthException("Invalid or expired reset token"));

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(resetToken);
            throw new AuthException("Reset token has expired");
        }

        AppUser user = resetToken.getUser();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Delete token after use
        tokenRepository.delete(resetToken);
    }

    private void sendResetEmail(String email, String token) {
        String resetLink = frontendUrl + "/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText(
                "You requested a password reset.\n\n" +
                        "Click the link below to reset your password:\n" +
                        resetLink + "\n\n" +
                        "This link will expire in " + expirationHours + " hours.\n\n" +
                        "If you did not request this, please ignore this email."
        );

        mailSender.send(message);
    }
}