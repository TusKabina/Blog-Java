package com.ivanrogulj.Blog.DTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import com.ivanrogulj.Blog.Entities.Like;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Long id;
    private String username;
    private String fullName;
    @JsonManagedReference
    private List<Like> likes;

}