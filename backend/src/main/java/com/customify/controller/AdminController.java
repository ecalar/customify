package com.customify.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/login")
    public String login() {
        return "login"; // Busca src/main/resources/templates/login.html
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard"; // Busca src/main/resources/templates/dashboard.html
    }

    @GetMapping("/products/new")
    public String newProduct() {
        return "product-form";
    }

    @GetMapping("/products/{id}/edit")
    public String editProduct() {
        return "product-edit";
    }
}