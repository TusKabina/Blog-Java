package com.ivanrogulj.Backend.Controllers;

import com.ivanrogulj.Backend.DTO.PostDTO;
import com.ivanrogulj.Backend.DTO.UserDTO;
import com.ivanrogulj.Backend.Entities.Role;
import com.ivanrogulj.Backend.Services.PostService;
import com.ivanrogulj.Backend.Services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.Optional;
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

        UserDTO loggedInUser = userService.getLoggedInUser();

        // Get a paginated list of posts for the logged-in user
        Page<PostDTO> postsPage = postService.getPostsByUser(loggedInUser.getId(), PageRequest.of(page, 2));

        String roleNameToFind = "ROLE_ADMIN";
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("postsPage", postsPage);
        Set<Role> roles = loggedInUser.getRoles();
         Role foundRole = roles.stream()
                .filter(role -> role.getName().equals(roleNameToFind))
                .findFirst().orElse(null);

         boolean isAdmin = false;
         if(foundRole != null)
         {
            isAdmin = true;
         }
        System.out.println("ROLE: "+loggedInUser.getRoles());
        model.addAttribute("loggedIn", true);
        model.addAttribute("isAdmin",isAdmin);
        return "home"; // This will render the "home.html" template
    }
}