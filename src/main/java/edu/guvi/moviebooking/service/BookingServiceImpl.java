package edu.guvi.moviebooking.service;

import edu.guvi.moviebooking.entity.Booking;
import edu.guvi.moviebooking.entity.BookingStatus;
import edu.guvi.moviebooking.entity.Seat;
import edu.guvi.moviebooking.entity.User;
import edu.guvi.moviebooking.repository.BookingRepository;
import edu.guvi.moviebooking.repository.SeatRepository;
import edu.guvi.moviebooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final SeatService seatService;
    private final SeatRepository seatRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @Override
    public Booking createBooking(Long showId, Long[] seatIds) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : null;

        if (username == null) {
            throw new RuntimeException("No authenticated user found while creating booking");
        }

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found for email: " + username));

        List<Seat> selectedSeats = seatRepository.findAllById(Arrays.asList(seatIds));

        // 🔥 FIXED: store correct user ID while booking
        seatService.bookSeats(seatIds, user.getId());

        Booking booking = new Booking();
        booking.setShowId(showId);
        booking.setStatus(BookingStatus.PENDING);
        booking.setSeatCount(seatIds.length);
        booking.setSeats(selectedSeats);
        booking.setUserId(user.getId());

        Booking savedBooking = bookingRepository.save(booking);

        for (Seat seat : selectedSeats) {
            seat.setBooking(savedBooking);
        }
        seatRepository.saveAll(selectedSeats);

        return savedBooking;
    }

    @Override
    public Booking getBooking(Long bookingId) {
        return bookingRepository.findById(bookingId).orElse(null);
    }

    @Override
    public Booking pay(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        booking.setStatus(BookingStatus.CONFIRMED);

        Booking updatedBooking = bookingRepository.save(booking);

        User user = userRepository.findById(booking.getUserId())
                .orElseThrow(() -> new RuntimeException("User data missing"));

        try {
            double amount = booking.getSeatCount() * 120;
            notificationService.sendBookingConfirmation(user.getEmail(), bookingId, amount);
            System.out.println("📩 EMAIL SENT SUCCESSFULLY to " + user.getEmail());
        } catch (Exception e) {
            System.out.println("❌ EMAIL FAILED: " + e.getMessage());
        }

        return updatedBooking;
    }

    @Override
    public Booking cancel(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        booking.setStatus(BookingStatus.CANCELLED);

        // 🔥 FIXED: release seats based on booking, not show
        seatService.cancelSeats(bookingId);

        Booking updatedBooking = bookingRepository.save(booking);

        User user = userRepository.findById(booking.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            notificationService.sendCancellationEmail(user.getEmail(), bookingId);
        } catch (Exception e) {
            System.out.println("❌ Cancellation email failed: " + e.getMessage());
        }

        return updatedBooking;
    }

    @Override
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    @Override
    public Booking updateBooking(Booking booking) {
        return bookingRepository.save(booking);
    }
}
