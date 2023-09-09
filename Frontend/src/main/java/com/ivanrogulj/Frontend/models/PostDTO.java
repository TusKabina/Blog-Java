package com.ivanrogulj.Frontend.models;

import com.fasterxml.jackson.annotation.JsonInclude;
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
