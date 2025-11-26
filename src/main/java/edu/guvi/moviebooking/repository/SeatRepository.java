package edu.guvi.moviebooking.repository;

import edu.guvi.moviebooking.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByShowId(Long showId);

    List<Seat> findByBookingId(Long bookingId);

}
