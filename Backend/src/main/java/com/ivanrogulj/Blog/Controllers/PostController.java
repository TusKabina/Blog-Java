package com.ivanrogulj.Blog.Controllers;

import com.ivanrogulj.Blog.DTO.CategoryDTO;
import com.ivanrogulj.Blog.DTO.PostDTO;
import com.ivanrogulj.Blog.DTO.UserDTO;
import com.ivanrogulj.Blog.Services.CategoryService;
import com.ivanrogulj.Blog.Services.PostService;
import com.ivanrogulj.Blog.Services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PostController {


    private final PostService postService;
    private final UserService userService;
    private final CategoryService categoryService;

    public PostController(PostService postService, UserService userService, CategoryService categoryService) {
        this.postService = postService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @GetMapping("/post/{postId}")
    public String viewPost(@PathVariable Long postId, Model model) {

        UserDTO loggedInUser = userService.getLoggedInUser();
        PostDTO post = postService.getPostById(postId);

        if (post != null) {
            model.addAttribute("post", post);
            model.addAttribute("loggedInUser",loggedInUser);
            model.addAttribute("loggedIn", true);

            boolean isAdmin = userService.isAdmin(loggedInUser);

            model.addAttribute("isAdmin",isAdmin);
            return "post";
        } else {
            return "error";
        }
    }

    @GetMapping("/post/update/{postId}")
    public String getUpdatePostForm(@PathVariable Long postId, Model model) {
        // Retrieve the post by ID and populate the update form
        PostDTO post = postService.getPostById(postId);
        UserDTO loggedInUser = userService.getLoggedInUser();
        List<CategoryDTO> categories = categoryService.getAllCategories();
        model.addAttribute("post", post);
        model.addAttribute("categories", categories);
        return "updatePost";
    }
    @PostMapping("/post/update/{postId}")
    public String updatePost(@PathVariable Long postId, @ModelAttribute PostDTO updatedPost) {

        PostDTO postDb = postService.getPostById(postId);
        updatedPost.setAuthor(postDb.getAuthor());
        postService.updatePost(postId, updatedPost);
        return "redirect:/post/{postId}";
    }


    @GetMapping("/post/create")
    public String getCreatePostForm(Model model) {
        UserDTO user = userService.getLoggedInUser();
        List<CategoryDTO> categories = categoryService.getAllCategories();
        model.addAttribute("userId", user.getId());
        model.addAttribute("categories", categories);
        model.addAttribute("post", new PostDTO());
        return "createPost";
    }

    @PostMapping("/post/create/{userId}")
    public String createPost(@PathVariable Long userId, @ModelAttribute PostDTO postDTO) {
        postService.createPost(postDTO, userId);
        return "redirect:/home";
    }

    @PostMapping("/post/delete/{postId}")
    public String deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return "redirect:/home";
    }
}