package com.home.controllers;

import com.home.dto.UserToken;
import com.home.entities.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute( "user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerPost(@ModelAttribute("user") User user, Model model ) {
        model.addAttribute("user", user);

        return "register-results";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute( "token", new UserToken());
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(@ModelAttribute("email") UserToken token, Model model) {
        model.addAttribute( "email", token.getEmail());
        System.out.println("Email is: " + token.getEmail());
        return "index";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
