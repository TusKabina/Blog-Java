package com.ivanrogulj.Frontend.models;


import lombok.Data;

@Data
public class UserLoginRequest {
    private String username;
    private String password;

}