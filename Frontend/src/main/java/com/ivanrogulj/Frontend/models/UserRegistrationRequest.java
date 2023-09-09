package com.ivanrogulj.Frontend.models;

import lombok.Data;

@Data
public class UserRegistrationRequest {
    private String username;
    private String password;
    private String fullName;
    private String email;

}