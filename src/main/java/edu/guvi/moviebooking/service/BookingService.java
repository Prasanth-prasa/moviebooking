package edu.guvi.moviebooking.service;

import edu.guvi.moviebooking.entity.Booking;

public interface BookingService {
    Booking createBooking(Long showId, Long[] seatIds);

    Booking getBooking(Long bookingId);

    Booking pay(Long bookingId);

    Booking cancel(Long bookingId);

    void cancelBooking(Long bookingId);


      Booking updateBooking(Booking booking); // <-- ADD THI
}
