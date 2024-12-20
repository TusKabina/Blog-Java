package com.ivanrogulj.Blog.Entities;

import com.ivanrogulj.Blog.Enums.RoleEnum;
import lombok.Data;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Data
@Table(name="roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}