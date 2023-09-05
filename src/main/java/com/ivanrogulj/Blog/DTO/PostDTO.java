package com.ivanrogulj.Blog.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ivanrogulj.Blog.Entities.Category;
import com.ivanrogulj.Blog.Entities.Comment;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDTO {

    private Long id;

    private UserDTO author;
    private String title;
    private String content;
    private CategoryDTO category;
    private List<CommentDTO> comments;
    private LocalDateTime creationDate;
}
