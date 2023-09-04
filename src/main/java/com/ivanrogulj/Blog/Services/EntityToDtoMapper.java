package com.ivanrogulj.Blog.Services;
import com.ivanrogulj.Blog.DTO.CategoryDTO;
import com.ivanrogulj.Blog.DTO.PostDTO;
import com.ivanrogulj.Blog.Entities.Category;
import com.ivanrogulj.Blog.Entities.Like;
import com.ivanrogulj.Blog.Entities.Post;
import com.ivanrogulj.Blog.Services.LikeService;
import com.ivanrogulj.Blog.DTO.UserDTO;
import com.ivanrogulj.Blog.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EntityToDtoMapper {




    public UserDTO convertUserToUserDTO(User user, List<Like> likes) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFullName(user.getFullName());
        userDTO.setLikes(likes);

        return userDTO;
    }

    public PostDTO convertToPostDto(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        // Map other properties as needed
        return postDTO;
    }

    public CategoryDTO convertToCategoryDto(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        // Map other properties as needed
        return categoryDTO;
    }
}