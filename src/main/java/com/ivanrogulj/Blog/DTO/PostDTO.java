package com.ivanrogulj.Blog.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDTO {

    private Long id;
    private String title;
    private String content;
    private CategoryDTO category;
}
