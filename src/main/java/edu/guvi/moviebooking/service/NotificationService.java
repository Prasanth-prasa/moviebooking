package edu.guvi.moviebooking.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    // ===============================
    // 🔹 INTERNAL REUSABLE EMAIL METHOD
    // ===============================
    private void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setFrom("Movie Booking System <prasanthprasa4@gmail.com>"); // 👈 important
            helper.setReplyTo("prasanthprasa4@gmail.com");

            message.addHeader("Return-Path", "prasanthprasa4@gmail.com"); 
            
            helper.setSubject(subject);
            helper.setText(body, true); // HTML

            mailSender.send(message);

            System.out.println("📩 Email sent to " + to);

        } catch (MessagingException e) {
            System.out.println("❌ Email error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to send email");
        }
    }

    // ===============================
    // 🔹 BOOKING CONFIRMATION EMAIL
    // ===============================
   public void sendBookingConfirmation(String to, Long bookingId, double amount) {

    String subject = "🎟 Your Movie Ticket Booking Confirmation #" + bookingId;

    String body =
            "<html>" +
            "<body style='font-family: Arial, sans-serif; background-color:#f6f6f6; padding:20px;'>" +
            "    <div style='max-width:500px; margin:auto; background:white; padding:20px; border-radius:10px;'>" +
            "        <h2 style='text-align:center; color:#4CAF50;'>Booking Confirmed 🎉</h2>" +
            "        <p>Thank you for booking your movie ticket with us!</p>" +
            "        <p><b>Booking ID:</b> " + bookingId + "</p>" +
            "        <p><b>Amount Paid:</b> ₹" + amount + "</p>" +
            "        <br>" +
            "        <p style='font-size:14px; color:gray;'>If you did not make this booking, please contact support.</p>" +
            "    </div>" +
            "</body>" +
            "</html>";

    System.out.println("🔥 Inside sendBookingConfirmation for: " + to);
    sendEmail(to, subject, body);
}


    // ===============================
    // 🔹 BOOKING CANCELLATION EMAIL
    // ===============================
    public void sendCancellationEmail(String to, Long bookingId) {

    String subject = "❌ Your Booking Has Been Cancelled";

    String body =
            "<html>" +
            "<body style='font-family: Arial, sans-serif; background-color:#f6f6f6; padding:20px;'>" +
            "    <div style='max-width:500px; margin:auto; background:white; padding:20px; border-radius:10px;'>" +
            "        <h2 style='text-align:center; color:#ff5252;'>Booking Cancelled</h2>" +
            "        <p>Your movie booking (<b>ID: " + bookingId + "</b>) has been cancelled.</p>" +
            "        <p style='font-size:14px; color:gray;'>If this was not you, please contact support.</p>" +
            "    </div>" +
            "</body>" +
            "</html>";

    sendEmail(to, subject, body);
}

}
