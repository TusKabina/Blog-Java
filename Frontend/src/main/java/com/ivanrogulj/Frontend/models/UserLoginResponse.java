package com.ivanrogulj.Frontend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponse {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private Set<Role> roles;


}
