package edu.guvi.moviebooking.controller;

import edu.guvi.moviebooking.entity.User;
import edu.guvi.moviebooking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // Show login page
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Show register page
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    // Handle register POST
    @PostMapping("/register")
    public String register(User user) {
        userService.register(user);
        return "redirect:/login?registered";
    }
}
