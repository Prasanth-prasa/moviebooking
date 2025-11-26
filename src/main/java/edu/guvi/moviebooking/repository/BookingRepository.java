package edu.guvi.moviebooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.guvi.moviebooking.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
