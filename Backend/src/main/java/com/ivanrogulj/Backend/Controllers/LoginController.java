package com.ivanrogulj.Backend.Controllers;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }


    @GetMapping("/login-error")
    public String login(Model model) {
        return "login";
    }

}