package com.ivanrogulj.Blog.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id")
   // @JsonManagedReference
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    //@JsonManagedReference
    private User user;
    private LocalDateTime creationDate;


}