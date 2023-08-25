package com.ivanrogulj.Blog.Controllers;

import com.ivanrogulj.Blog.Entities.Like;
import com.ivanrogulj.Blog.Services.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/{userId}/{postId}")
    public ResponseEntity<Like> likePost(@PathVariable Long userId, @PathVariable Long postId) {
        Like like = likeService.likePost(userId, postId);
        if (like != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(like);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{likeId}")
    public ResponseEntity<Void> unlikePost(@PathVariable Long likeId) {
        likeService.unlikePost(likeId);
        return ResponseEntity.noContent().build();
    }
}