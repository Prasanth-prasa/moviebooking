package edu.guvi.moviebooking.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {

    @GetMapping("/postLogin")
    public String postLogin(Authentication authentication) {

        if(authentication == null){
            return "redirect:/login?error";
        }

        // Check roles
        if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            return "redirect:/admin/dashboard";
        }

        return "redirect:/home";
    }
}
