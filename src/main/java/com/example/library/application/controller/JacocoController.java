package com.example.library.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JacocoController {

    @GetMapping("/coverage")
    public String coverage() {
        // Redirect so browser URL matches resource handler
        return "redirect:/coverage/index.html";
    }
}
