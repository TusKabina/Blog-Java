package com.ivanrogulj.Blog.DTO;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO {

    private Long id;
    private String content;
    private UserDTO user;
    private LocalDateTime creationDate;
}
