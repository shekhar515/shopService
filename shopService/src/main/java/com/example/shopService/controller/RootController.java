package com.example.shopService.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Root controller to handle base paths and provide navigation
 */
@Controller
public class RootController {

    /**
     * Handle root path - redirect to API documentation
     */
    @GetMapping("/")
    public String root() {
        return "redirect:/swagger-ui.html";
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
