package com.ivanrogulj.Backend.Controllers;

import com.ivanrogulj.Backend.DTO.PostDTO;
import com.ivanrogulj.Backend.DTO.UserDTO;
import com.ivanrogulj.Backend.Entities.Role;
import com.ivanrogulj.Backend.Services.PostService;
import com.ivanrogulj.Backend.Services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
public class PostController {


    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/post/{postId}")
    public String viewPost(@PathVariable Long postId, Model model) {

        UserDTO loggedInUser = userService.getLoggedInUser();
        // Fetch the post by postId
        PostDTO post = postService.getPostById(postId);

        if (post != null) {
            // Add the post to the model
            model.addAttribute("post", post);
            model.addAttribute("loggedInUser",loggedInUser);
            model.addAttribute("loggedIn", true);

            String roleNameToFind = "ROLE_ADMIN";
            Set<Role> roles = loggedInUser.getRoles();
            Role foundRole = roles.stream()
                    .filter(role -> role.getName().equals(roleNameToFind))
                    .findFirst().orElse(null);

            boolean isAdmin = false;
            if(foundRole != null)
            {
                isAdmin = true;
            }
            model.addAttribute("isAdmin",isAdmin);
            return "post"; // Render the "post.html" template
        } else {
            // Handle the case where the post is not found, e.g., show an error page
            return "error"; // Create an "error.html" template for error handling
        }
    }

    @GetMapping("/post/update/{postId}")
    public String getUpdatePostForm(@PathVariable Long postId, Model model) {
        // Retrieve the post by ID and populate the update form
        PostDTO post = postService.getPostById(postId);
        model.addAttribute("post", post);
        return "updatePost"; // Create an "update-post.html" template for the update form
    }
    @PutMapping("/post/update/{postId}")
    public String updatePost(@PathVariable Long postId, @ModelAttribute PostDTO updatedPost) {

        postService.updatePost(postId, updatedPost);
        return "redirect:/post/{postId}";
    }


    @GetMapping("/post/create")
    public String getCreatePostForm(Model model) {
        UserDTO user = userService.getLoggedInUser();
        model.addAttribute("userId", user.getId());
        model.addAttribute("post", new PostDTO());
        return "createPost";
    }

    @PostMapping("/post/create/{userId}")
    public String createPost(@PathVariable Long userId, @ModelAttribute PostDTO postDTO) {
        postService.createPost(postDTO, userId);
        return "redirect:/home";
    }
}