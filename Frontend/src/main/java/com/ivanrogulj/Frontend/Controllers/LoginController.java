package com.ivanrogulj.Frontend.Controllers;

import com.ivanrogulj.Frontend.models.UserDTO;
import com.ivanrogulj.Frontend.models.UserLoginRequest;
import com.ivanrogulj.Frontend.models.UserLoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
public class LoginController {
    private final RestTemplate restTemplate;

    @Autowired
    public LoginController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("userLoginRequest", new UserLoginRequest()); // Add an empty User object
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("userLoginRequest") UserLoginRequest userLoginRequest, Model model) {

        // Send the request to your API's login endpoint

        System.out.println("Username: " +userLoginRequest.getUsername());
        System.out.println("Username: " +userLoginRequest.getPassword());
        ResponseEntity<UserLoginResponse> response = restTemplate.postForEntity(
                "http://127.0.0.1:8080/login", // Replace with your API's login endpoint
                userLoginRequest,
                UserLoginResponse.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return "redirect:/secured-page";
        } else {
            System.out.println("logging in failed man....");
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }
}