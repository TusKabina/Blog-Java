package com.ivanrogulj.Frontend.Controllers;


import com.ivanrogulj.Frontend.models.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class HomeController {

    private final RestTemplate restTemplate;

    public HomeController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/home")
    public String home(Model model, @RequestParam(defaultValue = "1") int page) {
//        int pageSize = 10; // Set your desired page size
//
//        UserDTO loggedInUser = restTemplate.getForObject("http://127.0.0.1:8080/api/posts/all/2?page=0&size=10", UserDTO.class);
//
//        // Make a request to your API for paginated data
//        String apiEndpoint = "http://127.0.0.1:8080/api/posts/all/2?page=0&size=10" + page + "&size=" + pageSize;
//
//        System.out.println("LoggedInUser "+loggedInUser.getUsername());

        return "profile";
    }
}