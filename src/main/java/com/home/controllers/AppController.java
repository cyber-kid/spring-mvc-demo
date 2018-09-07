package com.home.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
