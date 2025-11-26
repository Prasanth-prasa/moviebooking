package edu.guvi.moviebooking.controller;

import edu.guvi.moviebooking.entity.Booking;
import edu.guvi.moviebooking.service.BookingService;
import edu.guvi.moviebooking.service.NotificationService;
import edu.guvi.moviebooking.service.SeatService;
import edu.guvi.moviebooking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final SeatService seatService;
    private final NotificationService notificationService;
    private final UserService userService;

    @GetMapping("/booking/summary")
    public String bookingSummary(@RequestParam Long bookingId, Model model) {
        model.addAttribute("booking", bookingService.getBooking(bookingId));
        return "booking/summary";
    }

    // @PostMapping("/pay/{bookingId}")
    // public String pay(@PathVariable Long bookingId) {
    //     bookingService.pay(bookingId);
    //     return "redirect:/booking/summary?bookingId=" + bookingId;
    // } // <-- Missing bracket FIXED HERE
@GetMapping("/booking/cancel/{bookingId}")
public String cancelBooking(@PathVariable Long bookingId, RedirectAttributes ra) {

    try {
        Booking booking = bookingService.getBooking(bookingId);

        if (booking == null) {
            ra.addFlashAttribute("error", "Booking not found!");
            return "redirect:/home";
        }

        // Cancel booking
        bookingService.cancel(bookingId);

        // Fetch user email using userId from booking
        String email = userService.getUserEmail(booking.getUserId());

        if (email != null) {
            notificationService.sendCancellationEmail(email, bookingId);
        }

        ra.addFlashAttribute("success", "Booking cancelled successfully!");

        return "redirect:/booking/summary?bookingId=" + bookingId;

    } catch (Exception e) {
        e.printStackTrace();
        ra.addFlashAttribute("error", "Failed to cancel booking!");
        return "redirect:/home";
    }
}

}
