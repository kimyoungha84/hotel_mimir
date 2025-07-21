package kr.co.sist.resvroom;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResExceptionHandler {

    @ExceptionHandler(org.thymeleaf.exceptions.TemplateInputException.class)
    public String handleTemplateError(Exception e) {
        return "redirect:/room_resv/error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralError(Exception e) {
        return "redirect:/room_resv/error";
    }
}