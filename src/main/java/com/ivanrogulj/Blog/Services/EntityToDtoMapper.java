package com.ivanrogulj.Blog.Services;
import com.ivanrogulj.Blog.Entities.Like;
import com.ivanrogulj.Blog.Services.LikeService;
import com.ivanrogulj.Blog.DTO.UserDTO;
import com.ivanrogulj.Blog.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EntityToDtoMapper {

    private final LikeService likeService;

    @Autowired
    public EntityToDtoMapper(LikeService likeService) {
        this.likeService = likeService;
    }

    public UserDTO convertUserToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFullName(user.getFullName());

        List<Like> likes = likeService.getLikesForUser(user.getId());
        userDTO.setLikes(likes);

        return userDTO;
    }
}