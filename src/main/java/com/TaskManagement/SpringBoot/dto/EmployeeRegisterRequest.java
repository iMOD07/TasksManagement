package com.TaskManagement.SpringBoot.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
public class EmployeeRegisterRequest {
    private String fullName;

    @Column(nullable = false)
    private String email;

    private String password;

    private String mobileNumber;

    @JsonProperty("department")
    private String department;

    @JsonProperty("jobTitle")
    private String jobTitle;

    @JsonProperty("role")
    private String role;
}
