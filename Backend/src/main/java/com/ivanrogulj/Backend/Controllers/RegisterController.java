package com.ivanrogulj.Backend.Controllers;

import com.ivanrogulj.Backend.DTO.UserRegistrationRequest;
import com.ivanrogulj.Backend.Entities.Role;
import com.ivanrogulj.Backend.Entities.User;
import com.ivanrogulj.Backend.Repositories.RoleRepository;
import com.ivanrogulj.Backend.Repositories.UserRepository;
import com.ivanrogulj.Backend.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
public class RegisterController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public RegisterController(UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userRegistrationRequest", new UserRegistrationRequest());
        return "register"; // Return the name of your registration HTML template
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userRegistrationRequest") UserRegistrationRequest userRegistrationRequest) {
        if (userRepository.existsByUsername(userRegistrationRequest.getUsername())) {
           return "error";
        }
        if(userRepository.existsByEmail(userRegistrationRequest.getEmail()))
        {
            return "error";
        }

        User user = new User();
        user.setUsername(userRegistrationRequest.getUsername());
        user.setFullName(userRegistrationRequest.getFullName());
        user.setPassword(passwordEncoder.encode(userRegistrationRequest.getPassword()));
        user.setEmail(userRegistrationRequest.getEmail());
        Role roles = roleRepository.findByName("ROLE_USER").get();
        user.setRoles(Collections.singleton(roles));
        userRepository.save(user);

        return "redirect:/login";
    }
}