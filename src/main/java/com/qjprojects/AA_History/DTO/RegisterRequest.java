package com.qjprojects.AA_History.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class RegisterRequest {
    @NotBlank
    @Size(max = 80)
    private String username;

    @Email
    @NotBlank
    @Size(max = 120)
    private String email;

    @Size(min = 8, max = 100)
    private String password;

    @Size(max = 100)
    private String name;


    private LocalDate birthDate;

    @Size(max = 20)
    private String phoneNumber;

    public RegisterRequest(String username, String email, String password, String name, LocalDate birthDate, String phoneNumber) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
