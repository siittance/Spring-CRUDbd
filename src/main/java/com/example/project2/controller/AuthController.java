package com.example.project2.controller;

import com.example.project2.model.User;
import com.example.project2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                          @RequestParam String password,
                          @RequestParam(defaultValue = "USER") String role) {
        if (userService.existsByUsername(username)) {
            return "redirect:/register?error=exists";
        }
        
        userService.createUserWithRole(username, password, role);
        return "redirect:/login?registered=true";
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }
}

