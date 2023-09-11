package com.ivanrogulj.Backend.Controllers;

import com.ivanrogulj.Backend.DTO.UserDTO;
import com.ivanrogulj.Backend.Services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class SearchController {

    private final UserService userService;

    public SearchController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/search")
    public String searchUsers(@RequestParam(name = "q", required = false) String query, @RequestParam(name = "page", defaultValue = "0") int page, Model model) {

        UserDTO loggedInUserDetails = userService.getLoggedInUser();
        boolean isAdmin = userService.isAdmin(loggedInUserDetails);
        Page<UserDTO> users = userService.searchUsersByFullName(query, PageRequest.of(page, 5));

        model.addAttribute("users", users);
        model.addAttribute("query", query);
        model.addAttribute("isAdmin",isAdmin);

        return "usersFilter"; // Return the name of your Thymeleaf template for search results
    }
}
