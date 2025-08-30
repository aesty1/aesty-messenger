package ru.denis.aestymes.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.denis.aestymes.dtos.LoginForm;

@Controller
public class AuthenticationController {

    @GetMapping("/login")
    public String login(Model model, LoginForm loginForm) {
        model.addAttribute("loginForm", loginForm);

        return "authentication/login";
    }

    @GetMapping("/register")
    public String register() {
        return "authentication/register";
    }
}
