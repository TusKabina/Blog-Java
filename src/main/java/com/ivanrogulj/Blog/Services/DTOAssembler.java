package com.ivanrogulj.Blog.Services;
import com.ivanrogulj.Blog.DTO.CategoryDTO;
import com.ivanrogulj.Blog.DTO.CommentDTO;
import com.ivanrogulj.Blog.DTO.PostDTO;
import com.ivanrogulj.Blog.Entities.*;
import com.ivanrogulj.Blog.DTO.UserDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DTOAssembler {




    public UserDTO convertUserToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        userDTO.setRoles(user.getRoles());

        return userDTO;
    }

    public User convertDtoToUser(UserDTO userDTO) {
        User user = new User();

        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setRoles(userDTO.getRoles());

        return user;
    }
    public CategoryDTO convertToCategoryDto(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();

        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());

        return categoryDTO;
    }

    public Category convertDtoToCategory(CategoryDTO categoryDTO) {
        Category category = new Category();

        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());

        return category;
    }

    public CommentDTO convertToCommentDto(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        UserDTO userDTO = convertUserToUserDTO(comment.getUser());

        commentDTO.setId(comment.getId());
        commentDTO.setUser(userDTO);
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreationDate(comment.getCreationDate());

        return commentDTO;
    }

    public Comment convertDtoToComment(CommentDTO commentDto) {
        Comment comment = new Comment();

        User user = convertDtoToUser(commentDto.getUser());

        comment.setId(commentDto.getId());
        comment.setUser(user);
        comment.setContent(commentDto.getContent());
        comment.setCreationDate(commentDto.getCreationDate());

        return comment;
    }

    public PostDTO convertToPostDto(Post post) {
        PostDTO postDTO = new PostDTO();
        UserDTO userDto = convertUserToUserDTO(post.getAuthor());
        if(post.getComments() != null)
        {
            List<CommentDTO> commentDTOs = post.getComments()
                    .stream()
                    .map(this::convertToCommentDto)
                    .toList();

            postDTO.setComments(commentDTOs);
        }

        postDTO.setId(post.getId());
        postDTO.setAuthor(userDto);
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());

        if(post.getCategory() != null)
        {
            CategoryDTO categoryDTO = convertToCategoryDto(post.getCategory());
            postDTO.setCategory(categoryDTO);
        }

        return postDTO;
    }

    public Post convertDtoToPost(PostDTO postDTO) {
        Post post = new Post();

        post.setId(postDTO.getId());
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setCreationDate(postDTO.getCreationDate());

        User author = convertDtoToUser(postDTO.getAuthor());
        post.setAuthor(author);
        if(postDTO.getCategory() != null)
        {
            Category category = convertDtoToCategory(postDTO.getCategory());
            post.setCategory(category);
        }
        if(postDTO.getComments() != null)
        {
            List<Comment> comments = postDTO.getComments()
                    .stream()
                    .map(this::convertDtoToComment)
                    .collect(Collectors.toList());
            post.setComments(comments);
        }


        return post;
    }






}