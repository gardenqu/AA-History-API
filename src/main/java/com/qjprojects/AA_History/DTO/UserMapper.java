package com.qjprojects.AA_History.DTO;

import com.qjprojects.AA_History.Entity.AppUser;
import com.qjprojects.AA_History.Entity.Role;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserResponse toResponse(AppUser user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getName(),
                user.getPhoneNumber(),
                user.getBirthDate() != null ? user.getBirthDate().toString() : null,
                user.getVerified(),
                user.getActive(),
                user.getBanned(),
                user.getProfileComplete(),
                user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet())
        );
    }
}