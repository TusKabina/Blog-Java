package com.ivanrogulj.Blog.DTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ivanrogulj.Blog.Entities.Role;
import lombok.Data;

import java.util.Set;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Long id;
    private String username;
    private String fullName;
    private Set<Role> roles;

}