package com.ivanrogulj.Blog.Entities;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
@Table(name="roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}