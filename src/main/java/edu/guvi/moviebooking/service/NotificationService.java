package edu.guvi.moviebooking.service;

import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Value("${MAIL_PASSWORD}") // Resend API Key
    private String apiKey;

    @Value("${MAIL_FROM}")
    private String fromEmail;

    public void sendBookingConfirmation(String toEmail, Long bookingId, double amount) {

        try {
            OkHttpClient client = new OkHttpClient();

            JSONObject emailJson = new JSONObject();
            emailJson.put("from", fromEmail);
            emailJson.put("to", toEmail);
            emailJson.put("subject", "🎬 Booking Confirmation - MovieBooking");
            emailJson.put("html",
                    "<h2>Booking Confirmed 🎉</h2>" +
                            "<p>Your booking ID: <strong>" + bookingId + "</strong></p>" +
                            "<p>Total Amount Paid: <strong>₹" + amount + "</strong></p>" +
                            "<p>Thank you for choosing MovieBooking!</p>"
            );

            RequestBody body = RequestBody.create(
                    emailJson.toString(),
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url("https://api.resend.com/emails")
                    .post(body)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .build();

            client.newCall(request).execute();
            System.out.println("📧 Email Sent Successfully via Resend");

        } catch (Exception e) {
            System.out.println("❌ Email sending failed: " + e.getMessage());
        }
    }
}
