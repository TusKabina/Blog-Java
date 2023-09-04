package com.ivanrogulj.Blog.DTO;

import com.ivanrogulj.Blog.Entities.Post;
import com.ivanrogulj.Blog.Entities.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private UserDTO user;
    private LocalDateTime creationDate;
}
