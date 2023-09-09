package com.ivanrogulj.Blog.Entities;

import jakarta.persistence.*;
import lombok.Data;



@Data
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

}