package com.ivanrogulj.Backend.Controllers;

import com.ivanrogulj.Backend.DTO.UserDTO;
import com.ivanrogulj.Backend.Services.CategoryService;
import com.ivanrogulj.Backend.Services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {


    private final UserService userService;


    private final CategoryService categoryService;

    public AdminController(UserService userService, CategoryService categoryService) {
        this.userService = userService;
        this.categoryService = categoryService;
    }


    @GetMapping
    public String adminDashboard(Model model) {
        UserDTO loggedInUserDetails = userService.getLoggedInUser();
        boolean isAdmin = userService.isAdmin(loggedInUserDetails);

        model.addAttribute("loggedInUser", loggedInUserDetails);
        model.addAttribute("isAdmin",isAdmin);
        model.addAttribute("userCount", userService.getUsersCount());
        return "admin";
    }
    @GetMapping("/view-users")
    public String viewUsers(Model model, @RequestParam(name = "page", defaultValue = "0") int page) {

        int size = 10;
        Page<UserDTO> usersPage = userService.getAllUsers(PageRequest.of(page, size));
        UserDTO loggedInUserDetails = userService.getLoggedInUser();
        boolean isAdmin = userService.isAdmin(loggedInUserDetails);

        model.addAttribute("loggedInUser", loggedInUserDetails);
        model.addAttribute("isAdmin",isAdmin);
        model.addAttribute("users", usersPage);

        return "viewUsers";
    }

    @PostMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/view-users";
    }
}