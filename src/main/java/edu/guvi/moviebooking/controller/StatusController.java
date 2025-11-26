package edu.guvi.moviebooking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatusController {

    @GetMapping("/email-status")
    public String emailStatus() {
        return "email-status"; // Must match your HTML file name
    }
}
