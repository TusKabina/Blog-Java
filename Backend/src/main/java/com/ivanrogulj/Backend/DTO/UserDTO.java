package com.ivanrogulj.Backend.DTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ivanrogulj.Backend.Entities.Role;
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