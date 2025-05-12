package com.TaskManagement.SpringBoot.dto;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
public class ClientRegisterRequest {
    private String fullName;
    private String email;
    private String password;
    private String mobileNumber;
    @JsonProperty("companyName")
    private String companyName;
    private String address;

}
