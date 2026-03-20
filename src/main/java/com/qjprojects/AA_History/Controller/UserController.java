package com.qjprojects.AA_History.Controller;

import com.qjprojects.AA_History.DTO.UpdateUserRequest;
import com.qjprojects.AA_History.DTO.UserMapper;
import com.qjprojects.AA_History.DTO.UserResponse;
import com.qjprojects.AA_History.Entity.AppUser;
import com.qjprojects.AA_History.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Get currently logged in user's profile
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal AppUser user) {
        return ResponseEntity.ok(UserMapper.toResponse(user));
    }


    // Update currently logged in user's profile
    @PatchMapping("/me")
    public ResponseEntity<UserResponse> updateMe(
            @AuthenticationPrincipal AppUser user,
            @RequestBody UpdateUserRequest request) {

        if (request.getName() != null) user.setName(request.getName());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        if (request.getBirthDate() != null) user.setBirthDate(request.getBirthDate());

        // Mark profile complete if all required fields are filled
        if (user.getName() != null && user.getBirthDate() != null && user.getPhoneNumber() != null) {
            user.setProfileComplete(true);
        }

        AppUser updated = userService.save(user);
        return ResponseEntity.ok(UserMapper.toResponse(updated));
    }
}