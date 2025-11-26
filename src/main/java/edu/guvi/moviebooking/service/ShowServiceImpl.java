package edu.guvi.moviebooking.service;

import edu.guvi.moviebooking.entity.Seat;
import edu.guvi.moviebooking.entity.SeatStatus;
import edu.guvi.moviebooking.entity.Show;
import edu.guvi.moviebooking.repository.SeatRepository;
import edu.guvi.moviebooking.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;

   @Override
public void save(Show show) {
    Show savedShow = showRepository.save(show);

    // Generate seats ONLY if not already created
    if (seatRepository.findByShowId(savedShow.getId()).isEmpty()) {

        for (int row = 1; row <= 5; row++) {
            for (int col = 1; col <= 10; col++) {

                Seat seat = new Seat();
                seat.setSeatNumber("R" + row + "-S" + col);
                seat.setStatus(SeatStatus.AVAILABLE);
                seat.setShow(savedShow);

                seatRepository.save(seat);
            }
        }
    }
}


    @Override
    public List<Show> getAll() {
        return showRepository.findAll();
    }

    @Override
    public Show getById(Long id) {
        return showRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        showRepository.deleteById(id);
    }

    @Override
    public List<Show> getByMovieId(Long movieId) {
        return showRepository.findByMovieId(movieId);
    }
}
