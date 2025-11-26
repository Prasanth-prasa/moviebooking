package edu.guvi.moviebooking;

import edu.guvi.moviebooking.entity.Booking;
import edu.guvi.moviebooking.entity.BookingStatus;
import edu.guvi.moviebooking.entity.User;
import edu.guvi.moviebooking.repository.BookingRepository;
import edu.guvi.moviebooking.repository.UserRepository;
import edu.guvi.moviebooking.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class BookingFlowTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void fullBookingFlow() {

        // Step 1: Create a dummy user so service doesn't fail
        User user = new User();
        user.setEmail("test@movie.com");
        user.setPassword("password"); // not relevant
        user = userRepository.save(user);

        // Step 2: Create a booking linked to that user
        Booking booking = new Booking();
        booking.setUserId(user.getId());
        booking.setShowId(1L);
        booking.setSeatCount(2);
        booking.setStatus(BookingStatus.PENDING);

        booking = bookingRepository.save(booking); // persisted with ID

        // Step 3: Perform real workflow
        Booking confirmed = bookingService.pay(booking.getId());

        // Step 4: Assertions
        assertThat(confirmed.getStatus()).isEqualTo(BookingStatus.CONFIRMED);
        assertThat(confirmed.getUserId()).isEqualTo(user.getId());
    }
}
