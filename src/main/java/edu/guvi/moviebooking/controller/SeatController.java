package edu.guvi.moviebooking.controller;

import edu.guvi.moviebooking.service.BookingService;
import edu.guvi.moviebooking.service.SeatService;
import edu.guvi.moviebooking.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/seats")
public class SeatController {

    private final SeatService seatService;
    private final ShowService showService;
    private final BookingService bookingService;  // ⭐ Added

    @GetMapping("/{showId}")
    public String selectSeats(@PathVariable Long showId, Model model) {
        model.addAttribute("show", showService.getById(showId));
        model.addAttribute("seats", seatService.getSeatsForShow(showId));
        return "seats/select";
    }

    @PostMapping("/book")
    public String bookSeats(
            @RequestParam(required = false, name = "seatIds") Long[] seatIds,
            @RequestParam Long showId,
            RedirectAttributes redirectAttributes) {

        if (seatIds == null || seatIds.length == 0) {
            redirectAttributes.addFlashAttribute("error", "⚠ Select at least one seat!");
            return "redirect:/seats/" + showId;
        }

        // 👉 Create booking entry
        var booking = bookingService.createBooking(showId, seatIds);

        redirectAttributes.addFlashAttribute("success", "🎉 Seats booked!");
        return "redirect:/booking/summary?bookingId=" + booking.getId();
    }
}
