package com.ivanrogulj.Backend.Controllers;

import com.ivanrogulj.Backend.DTO.UserDTO;
import com.ivanrogulj.Backend.Entities.User;
import com.ivanrogulj.Backend.Services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/update-user")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String showUpdateForm(Model model) {
        UserDTO loggedInUser = userService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);
        model.addAttribute("userId", loggedInUser.getId());
        return "updateUser";
    }

    @PostMapping("/{id}")
    public String updateUser(@PathVariable Long id, UserDTO user) {
        userService.updateUser(id, user);
        return "redirect:/home";
    }
}