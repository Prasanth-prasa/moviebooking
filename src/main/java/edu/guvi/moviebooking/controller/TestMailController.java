package edu.guvi.moviebooking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.guvi.moviebooking.service.NotificationService;

@RestController
@RequestMapping("/test-mail")
public class TestMailController {

    private final NotificationService notificationService;

    public TestMailController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public String sendTestEmail() {
        notificationService.sendBookingConfirmation(
                "prasanthprasa4@gmail.com", 
                9999L, 
                240.0
        );
        return "Test email sent!";
    }
}
