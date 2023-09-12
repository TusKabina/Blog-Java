package com.ivanrogulj.Backend.Controllers;

import com.ivanrogulj.Backend.DTO.CategoryDTO;
import com.ivanrogulj.Backend.DTO.PostDTO;
import com.ivanrogulj.Backend.DTO.UserDTO;
import com.ivanrogulj.Backend.Entities.Role;
import com.ivanrogulj.Backend.Services.CategoryService;
import com.ivanrogulj.Backend.Services.PostService;
import com.ivanrogulj.Backend.Services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;
import java.util.Set;

@Controller
public class HomeController {

    private final PostService postService;
    private final UserService userService;


    public HomeController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/home")
    public String home(Model model, @RequestParam(name = "page", defaultValue = "0") int page) {

        UserDTO loggedInUserDetails = userService.getLoggedInUser();
        Page<PostDTO> postsPage = postService.getPostsByUser(loggedInUserDetails.getId(), PageRequest.of(page, 2));

        boolean isAdmin = userService.isAdmin(loggedInUserDetails);

        model.addAttribute("loggedInUser", loggedInUserDetails);
        model.addAttribute("postsPage", postsPage);
        model.addAttribute("isAdmin",isAdmin);

        return "home";
    }


    @GetMapping("/profile/{userId}")
    public String userProfile(@PathVariable Long userId, Model model, @RequestParam(name = "page", defaultValue = "0") int page) {
        UserDTO userProfile = userService.getUserById(userId);
        UserDTO loggedInUserDetails = userService.getLoggedInUser();

        if(loggedInUserDetails.getId().equals(userId))
        {
            return "redirect:/home";
        }

        Page<PostDTO> postsPage = postService.getPostsByUser(userProfile.getId(), PageRequest.of(page, 2));
        boolean isAdmin = userService.isAdmin(loggedInUserDetails);

        model.addAttribute("loggedInUser",loggedInUserDetails);
        model.addAttribute("userProfile", userProfile);
        model.addAttribute("userPostsPage", postsPage);
        model.addAttribute("isAdmin",isAdmin);

        return "profile";
    }
}