package com.ivanrogulj.Frontend.models;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Set;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private Set<Role> roles;

}