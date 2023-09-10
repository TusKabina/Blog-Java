package com.ivanrogulj.Frontend.Controllers;

import com.ivanrogulj.Frontend.models.UserDTO;
import com.ivanrogulj.Frontend.models.UserLoginRequest;
import com.ivanrogulj.Frontend.models.UserLoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
        model.addAttribute("userLoginRequest", new UserLoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("userLoginRequest") UserLoginRequest userLoginRequest, Model model) {

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("username", userLoginRequest.getUsername());
        requestBody.add("password", userLoginRequest.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<UserLoginResponse> response = restTemplate.exchange(
                "http://127.0.0.1:8080/login",
                HttpMethod.POST,
                requestEntity,
                UserLoginResponse.class
        );

        if (response.getStatusCode() == HttpStatus.FOUND) {
            return "/hello";
        } else {
            System.out.println("Logging in failed...");
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }
}