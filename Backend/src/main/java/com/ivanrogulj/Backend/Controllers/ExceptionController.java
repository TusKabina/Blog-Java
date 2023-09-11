package com.ivanrogulj.Backend.Controllers;

import com.ivanrogulj.Backend.ExceptionHandler.BadRequestException;
import com.ivanrogulj.Backend.ExceptionHandler.DataNotFoundException;
import com.ivanrogulj.Backend.ExceptionHandler.ForbiddenException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = {BadRequestException.class})
    public String handleBadRequestException(BadRequestException e, Model model) {
        String errorMessage = e.getLocalizedMessage();
        model.addAttribute("errorMessage", errorMessage);
        return "somethingWentWrong";
    }

    @ExceptionHandler(value ={DataNotFoundException.class})
    public String handleDataNotFoundException(DataNotFoundException e, Model model) {
        String errorMessage = e.getLocalizedMessage();
        model.addAttribute("errorMessage", errorMessage);
        return "dataNotFound";
    }

    @ExceptionHandler(value ={ForbiddenException.class})
    public String handleForbiddenException(ForbiddenException e, Model model) {
        String errorMessage = e.getLocalizedMessage();
        model.addAttribute("errorMessage", errorMessage);
        return "unauthorized";
    }
}
