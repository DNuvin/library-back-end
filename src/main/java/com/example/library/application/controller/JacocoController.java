package com.example.library.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JacocoController {

    @GetMapping("/coverage")
    public String coverage() {
        // forward to the resource handler path
        return "forward:/coverage/index.html";
    }

}

