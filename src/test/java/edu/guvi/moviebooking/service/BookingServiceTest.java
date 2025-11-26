package edu.guvi.moviebooking.service;

import edu.guvi.moviebooking.entity.Booking;
import edu.guvi.moviebooking.entity.BookingStatus;
import edu.guvi.moviebooking.entity.User;
import edu.guvi.moviebooking.repository.BookingRepository;
import edu.guvi.moviebooking.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private SeatService seatService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    BookingServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPayShouldConfirmBooking() {

        // Mock Booking
        Booking mockBooking = new Booking();
        mockBooking.setId(1L);
        mockBooking.setStatus(BookingStatus.PENDING);
        mockBooking.setSeatCount(2);
        mockBooking.setUserId(10L);

        // Mock User
        User mockUser = new User();
        mockUser.setId(10L);
        mockUser.setEmail("test@mail.com");

        // Define mock behavior
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(mockBooking));
        when(userRepository.findById(10L)).thenReturn(Optional.of(mockUser));
        when(bookingRepository.save(any())).thenReturn(mockBooking);  // return modified object

        // Execute method
        Booking result = bookingService.pay(1L);

        // Validate result
        assertThat(result.getStatus()).isEqualTo(BookingStatus.CONFIRMED);

        // Ensure email sent
        verify(notificationService, times(1))
                .sendBookingConfirmation(eq("test@mail.com"), eq(1L), anyDouble());
    }
}
