package com.ivanrogulj.Frontend.Controllers;

import com.ivanrogulj.Frontend.models.UserRegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final RestTemplate restTemplate;

//    @Value("${backend.api.url}") // Define the backend API URL in your application properties
//    private String backendApiUrl;

    @Autowired
    public RegisterController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String registrationForm(Model model) {
        model.addAttribute("userRegistrationRequest", new UserRegistrationRequest());
        return "register";
    }

    @PostMapping
    public String registerUser(@ModelAttribute("userRegistrationRequest") UserRegistrationRequest userRegistrationRequest, Model model) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserRegistrationRequest> requestEntity = new HttpEntity<>(userRegistrationRequest, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                 "http://127.0.0.1:8080/auth/register",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
            return "redirect:/login";
        } else if (responseEntity.getStatusCode() == HttpStatus.CONFLICT) {
            model.addAttribute("usernameExists", true);
            return "register";
        } else {

            // Handle other error cases
            model.addAttribute("registrationError", true);
            return "register";
        }
    }
}
