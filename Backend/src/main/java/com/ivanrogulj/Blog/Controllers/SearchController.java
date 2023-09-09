package com.ivanrogulj.Blog.Controllers;

import com.ivanrogulj.Blog.DTO.PostDTO;
import com.ivanrogulj.Blog.DTO.UserDTO;
import com.ivanrogulj.Blog.Services.PostService;
import com.ivanrogulj.Blog.Services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
public class SearchController {


    private final UserService userService;

    private final PostService postService;

    public SearchController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("/users/fullName")
    public ResponseEntity<Page<UserDTO>> searchUsersByFullName(@RequestParam("query") String query, @PageableDefault(page = 0, size = 5) Pageable pageable) {
        Page<UserDTO> usersPage = userService.searchUsersByFullName(query, pageable);
        return ResponseEntity.ok(usersPage);
    }

    @GetMapping("/users/username")
    public ResponseEntity<UserDTO> searchUsersByUsername(@RequestParam("query") String query) {
        UserDTO usersPage = userService.searchUsersByUsername(query);
        return ResponseEntity.ok(usersPage);
    }

    @GetMapping("/posts")
    public ResponseEntity<Page<PostDTO>> searchPostsByTitle(@RequestParam("query") String query, @PageableDefault(page = 0, size = 5) Pageable pageable) {
        Page<PostDTO> postsPage = postService.searchPostsByTitle(query, pageable);
        return ResponseEntity.ok(postsPage);
    }
}