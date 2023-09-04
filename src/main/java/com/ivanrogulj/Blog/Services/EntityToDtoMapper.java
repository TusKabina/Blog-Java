package com.ivanrogulj.Blog.Services;
import com.ivanrogulj.Blog.DTO.CategoryDTO;
import com.ivanrogulj.Blog.DTO.CommentDTO;
import com.ivanrogulj.Blog.DTO.PostDTO;
import com.ivanrogulj.Blog.Entities.*;
import com.ivanrogulj.Blog.Services.LikeService;
import com.ivanrogulj.Blog.DTO.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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
    public CategoryDTO convertToCategoryDto(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();

        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());

        return categoryDTO;
    }

    public CommentDTO convertToCommentDto(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        UserDTO userDTO = convertUserToUserDTO(comment.getUser(),comment.getUser().getLikes());

        commentDTO.setId(comment.getId());
        commentDTO.setUser(userDTO);
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreationDate(comment.getCreationDate());

        return commentDTO;
    }

    public PostDTO convertToPostDto(Post post) {
        PostDTO postDTO = new PostDTO();
        CategoryDTO categoryDTO = convertToCategoryDto(post.getCategory());
        UserDTO userDto = convertUserToUserDTO(post.getAuthor(),post.getAuthor().getLikes());
        List<CommentDTO> commentDTOs = post.getComments()
                .stream()
                .map(this::convertToCommentDto)
                .toList();

        postDTO.setId(post.getId());
        postDTO.setAuthor(userDto);
        postDTO.setTitle(post.getTitle());
        postDTO.setComments(commentDTOs);
        postDTO.setCategory(categoryDTO);

        return postDTO;
    }






}