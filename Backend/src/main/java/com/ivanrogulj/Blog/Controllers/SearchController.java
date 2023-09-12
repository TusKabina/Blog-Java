package com.ivanrogulj.Blog.Controllers;

import com.ivanrogulj.Blog.DTO.UserDTO;
import com.ivanrogulj.Blog.Services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    private final UserService userService;


    public SearchController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/search")
    public String searchUsers(@RequestParam(name = "q", required = false) String query,
                              @RequestParam(name = "searchType", required = false, defaultValue = "user") String searchType,
                              @RequestParam(name = "page", defaultValue = "0") int page, Model model) {

        UserDTO loggedInUserDetails = userService.getLoggedInUser();
        boolean isAdmin = userService.isAdmin(loggedInUserDetails);
        Page<UserDTO> users = userService.searchUsersByFullName(query, PageRequest.of(page, 5));

        model.addAttribute("loggedInUser", loggedInUserDetails);
        model.addAttribute("users", users);
        model.addAttribute("query", query);
        model.addAttribute("isAdmin", isAdmin);

        return "usersFilter";
    }
}
