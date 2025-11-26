package edu.guvi.moviebooking.service;

import edu.guvi.moviebooking.entity.Seat;
import edu.guvi.moviebooking.entity.SeatStatus;
import edu.guvi.moviebooking.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository repo;

    public List<Seat> getSeatsForShow(Long showId) {
        return repo.findByShowId(showId);
    }

    // Updated to support Long[] or List<Long> smoothly
    public void bookSeats(Long[] seatIds, Long userId) {
        List<Long> ids = Arrays.asList(seatIds);

        for (Long id : ids) {
            Seat seat = repo.findById(id).orElseThrow();

            if (seat.getStatus() == SeatStatus.BOOKED)
                continue; // avoid double booking

            seat.setStatus(SeatStatus.BOOKED);
            seat.setBookedBy(userId);
            repo.save(seat);
        }
    }

    // public void releaseSeats(Long showId) {
    //     List<Seat> seats = repo.findByShowId(showId);
    //     for (Seat seat : seats) {
    //         if (seat.getStatus() == SeatStatus.BOOKED) {
    //             seat.setStatus(SeatStatus.AVAILABLE);
    //             seat.setBookedBy(null);
    //             repo.save(seat);
    //         }
    //     }
    // }

    public void cancelSeats(Long bookingId) {
        List<Seat> seats = repo.findByBookingId(bookingId);

        for (Seat seat : seats) {
            seat.setStatus(SeatStatus.AVAILABLE);
            seat.setBookedBy(null);
            seat.setBooking(null);
            repo.save(seat);
        }
    }

}
