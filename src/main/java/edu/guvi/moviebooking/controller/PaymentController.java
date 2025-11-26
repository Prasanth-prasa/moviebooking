package edu.guvi.moviebooking.controller;

import edu.guvi.moviebooking.entity.Booking;
import edu.guvi.moviebooking.service.BookingService;
import edu.guvi.moviebooking.service.PayPalService;
import edu.guvi.moviebooking.service.UserService;
import edu.guvi.moviebooking.service.NotificationService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final BookingService bookingService;
    private final PayPalService payPalService;
    private final NotificationService notificationService;
    private final UserService userService;

    // Render deployment URL (NO localhost)
    private static final String BASE_URL = "https://moviebooking-f5av.onrender.com";

    @GetMapping("/pay/{bookingId}")
    public String pay(@PathVariable Long bookingId) {

        Booking booking = bookingService.getBooking(bookingId);

        try {
            String approvalUrl = payPalService.createPayment(
                    "ORDER_" + booking.getId(),
                    booking.getSeatCount() * 120);

            return "redirect:" + approvalUrl;

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/booking/summary?bookingId=" + bookingId;
        }
    }

    @GetMapping("/success")
    public String success(
            @RequestParam(name = "token", required = false) String token,
            @RequestParam(name = "orderId", required = false) String orderId,
            RedirectAttributes ra,
            Model model) {

        boolean emailSent = false;
        Long bookingId = null;

        try {
            String paypalOrderId = token != null ? token : orderId;

            if (paypalOrderId == null) {
                throw new IllegalArgumentException("No PayPal order identifier received.");
            }

            com.paypal.orders.Order captured = payPalService.capturePayment(paypalOrderId);

            bookingId = Long.valueOf(
                    (orderId != null && orderId.startsWith("ORDER_"))
                            ? orderId.replace("ORDER_", "")
                            : captured.purchaseUnits().get(0).referenceId().replace("ORDER_", "")
            );

            Booking updatedBooking = bookingService.pay(bookingId);

            String userEmail = userService.getUserEmail(updatedBooking.getUserId());

            if (userEmail != null && !userEmail.isEmpty()) {
                notificationService.sendBookingConfirmation(
                        userEmail,
                        bookingId,
                        updatedBooking.getSeatCount() * 120
                );
                emailSent = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Redirect with query parameters
        return "redirect:" + BASE_URL 
                + "/email-status?status=" + (emailSent ? "success" : "failed") 
                + "&bookingId=" + bookingId;
    }

    @GetMapping("/cancel")
    public String cancel(RedirectAttributes ra) {
        ra.addFlashAttribute("error", "Payment cancelled!");
        return "redirect:/home";
    }
}
