package com.ivanrogulj.Blog.Controllers;

import com.ivanrogulj.Blog.Entities.Post;
import com.ivanrogulj.Blog.Services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        if (post != null) {
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all/{authorId}")
    public ResponseEntity<List<Post>> getPostByAuthorId(@PathVariable Long authorId) {
        List<Post> posts = postService.getPostByUser(authorId);
        if (posts != null) {
            return ResponseEntity.ok(posts);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{authorId}")
    public ResponseEntity<Post> createPost(@RequestBody Post post, @PathVariable Long authorId) {
        Post newPost = postService.createPost(post, authorId);
        if (newPost != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(newPost);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post post) {
        Post updatedPost = postService.updatePost(id, post);
        if (updatedPost != null) {
            return ResponseEntity.ok(updatedPost);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id, Authentication authentication) {
        if (isAuthorizedToDelete(authentication, id)) {
            // Delete the post
            postService.deletePost(id);
            return ResponseEntity.ok("Post deleted successfully.");
        } else {
            // Return a 403 Forbidden response for unauthorized access
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this post.");
        }
    }


    public boolean isAuthorizedToDelete(Authentication authentication, Long id) {
        // Check if the user has the "ADMIN" role
        Post post = postService.getPostById(id);
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        // Check if the user is the owner of the post or is an admin
        boolean isOwnerOrAdmin = post.getAuthor().getUsername().equals(authentication.getName()) || isAdmin;

        return isOwnerOrAdmin;
    }
}