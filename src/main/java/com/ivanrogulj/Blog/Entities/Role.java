package com.ivanrogulj.Blog.Entities;

import com.ivanrogulj.Blog.Enums.RoleEnum;
import lombok.Data;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Data
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleEnum name;

    @Override
    public String getAuthority() {
        return "ROLE_" + name.toString();
    }
}