package com.ivanrogulj.Blog.Controllers;

import com.ivanrogulj.Blog.DTO.PostDTO;
import com.ivanrogulj.Blog.Entities.Post;
import com.ivanrogulj.Blog.Services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<Page<PostDTO>> getAllPosts(@PageableDefault(page = 0, size = 5) Pageable pageable) {
        return ResponseEntity.ok(postService.getAllPosts(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
        PostDTO postDTO = postService.getPostDtoById(id);
        if (postDTO != null) {
            return ResponseEntity.ok(postDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<Page<PostDTO>> getPostByCategoryId(@PathVariable Long categoryId, @PageableDefault(page = 0, size = 5) Pageable pageable) {
        Page<PostDTO> posts = postService.getPostsByCategory(categoryId, pageable);
        if (posts != null) {
            return ResponseEntity.ok(posts);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{postId}/categories/assign/{categoryId}")
    public ResponseEntity<PostDTO> assignCategoryToPost(@PathVariable Long postId, @PathVariable Long categoryId) {
        PostDTO postDTO = postService.assignCategoryToPost(postId, categoryId);
        if (postDTO != null) {
            return ResponseEntity.ok(postDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all/{authorId}")
    public ResponseEntity<Page<PostDTO>> getPostsByAuthorId(@PathVariable Long authorId, @PageableDefault(page = 0, size = 5) Pageable pageable) {
        Page<PostDTO> posts = postService.getPostsByUser(authorId, pageable);
        if (posts != null) {
            return ResponseEntity.ok(posts);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{authorId}")
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO, @PathVariable Long authorId) {
        PostDTO newPost = postService.createPost(postDTO, authorId);
        if (newPost != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(newPost);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(@PathVariable Long id, @RequestBody PostDTO postDTO) {
        PostDTO updatedPost = postService.updatePost(id, postDTO);
        if (updatedPost != null) {
            return ResponseEntity.ok(updatedPost);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(Authentication authentication, @PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok("Post deleted successfully.");
    }



}