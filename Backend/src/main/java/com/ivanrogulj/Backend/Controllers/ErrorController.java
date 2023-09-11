package com.ivanrogulj.Backend.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public String handleError(Model model, Error error) {
        String errorMessage = "Something went wrong...";
        model.addAttribute("errorMessage", errorMessage);
        return "somethingWentWrong";
    }
}
