package com.ivanrogulj.Blog.Services;

import com.ivanrogulj.Blog.Entities.Like;
import com.ivanrogulj.Blog.Entities.Post;
import com.ivanrogulj.Blog.Entities.User;
import com.ivanrogulj.Blog.Repositories.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public LikeService(LikeRepository likeRepository, UserService userService, PostService postService) {
        this.likeRepository = likeRepository;
        this.userService = userService;
        this.postService = postService;
    }

    public Like likePost(Long userId, Long postId) {
        User user = userService.getUserById(userId);
        Post post = postService.getPostById(postId);

        if (user != null && post != null) {
            Like like = new Like();
            like.setUser(user);
            like.setPost(post);
            return likeRepository.save(like);
        }

        return null;
    }

    public void unlikePost(Long likeId) {
        likeRepository.deleteById(likeId);
    }
}