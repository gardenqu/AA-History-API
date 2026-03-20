package com.qjprojects.AA_History.DTO;

import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class UpdateUserRequest {

    @Size(max = 100)
    private String name;

    @Size(max = 20)
    private String phoneNumber;

    private LocalDate birthDate;

    public UpdateUserRequest(String name, String phoneNumber, LocalDate birthDate) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
    }

    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }
    public LocalDate getBirthDate() { return birthDate; }
}