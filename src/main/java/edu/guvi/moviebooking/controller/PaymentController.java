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

        try {
            // PayPal sometimes returns only token, so handle fallback logic
            String paypalOrderId = token != null ? token : orderId;

            if (paypalOrderId == null) {
                throw new IllegalArgumentException("No PayPal order identifier received.");
            }

            com.paypal.orders.Order captured = payPalService.capturePayment(paypalOrderId);

            String txnId = captured.purchaseUnits().get(0)
                    .payments()
                    .captures()
                    .get(0)
                    .id();

            // Extract only the booking ID from stored format ORDER_xx
            Long bookingId = Long.valueOf((orderId != null ? orderId : ""));
            if (bookingId == null || bookingId == 0L) {
                bookingId = Long.valueOf(captured.purchaseUnits().get(0).referenceId().replace("ORDER_", ""));
            }

            Booking updatedBooking = bookingService.pay(bookingId);

            String userEmail = userService.getUserEmail(updatedBooking.getUserId());

            System.out.println("📧 Sending email to: " + userEmail);

            if (userEmail != null && !userEmail.isEmpty()) {
                notificationService.sendBookingConfirmation(
                        userEmail,
                        bookingId,
                        updatedBooking.getSeatCount() * 120);

                emailSent = true;
            }

            model.addAttribute("transactionId", txnId);
            model.addAttribute("bookingId", bookingId);
            model.addAttribute("emailStatus", emailSent ? "success" : "failed");

            ra.addFlashAttribute("success", "Payment Successful!");

        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("error", "Payment processing failed!");
            model.addAttribute("emailStatus", "failed");
        }

        return "email-status";
    }

    @GetMapping("/cancel")
    public String cancel(RedirectAttributes ra) {
        ra.addFlashAttribute("error", "Payment cancelled!");
        return "redirect:/home";
    }
}
