package edu.guvi.moviebooking.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    @Value("${MAIL_FROM:no-reply@moviebooking.com}")
    private String fromEmail;

    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            helper.setFrom(fromEmail);

            mailSender.send(message);
            System.out.println("📩 Email sent successfully to: " + to);

        } catch (Exception e) {
            System.out.println("❌ Failed to send email: " + e.getMessage());
        }
    }

    public void sendBookingConfirmation(String to, Long bookingId, double amount) {

        String subject = "🎟 Booking Confirmed";

        String body =
                "<html><body style='font-family:Arial;padding:20px;background:#f4f4f4'>" +
                "<div style='max-width:550px;margin:auto;background:white;padding:20px;border-radius:10px'>" +
                "<h2 style='text-align:center;color:#28a745'>Booking Confirmed 🎉</h2>" +
                "<p>Your booking was successful!</p>" +
                "<p><b>Booking ID:</b> " + bookingId + "</p>" +
                "<p><b>Total Paid:</b> ₹" + amount + "</p>" +
                "<br><p style='font-size:13px;color:gray'>Enjoy the movie! 🍿</p>" +
                "</div></body></html>";

        sendEmail(to, subject, body);
    }

    public void sendCancellationEmail(String to, Long bookingId) {

        String subject = "❌ Booking Cancelled";

        String body =
                "<html><body style='font-family:Arial;padding:20px;background:#ffe6e6'>" +
                "<div style='max-width:550px;margin:auto;background:white;padding:20px;border-radius:10px'>" +
                "<h2 style='text-align:center;color:#dc3545'>Booking Cancelled ❌</h2>" +
                "<p>Your booking was cancelled successfully.</p>" +
                "<p><b>Booking ID:</b> " + bookingId + "</p>" +
                "<br><p style='font-size:13px;color:gray'>If accidental, rebook anytime.</p>" +
                "</div></body></html>";

        sendEmail(to, subject, body);
    }
}
