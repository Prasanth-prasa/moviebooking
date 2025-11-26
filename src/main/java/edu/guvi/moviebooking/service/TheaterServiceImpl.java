package edu.guvi.moviebooking.service;

import edu.guvi.moviebooking.entity.Theater;
import edu.guvi.moviebooking.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TheaterServiceImpl implements TheaterService {

    private final TheaterRepository theaterRepository;

    @Override
    public void save(Theater theater) {
        if (theater.getTotalRows() != null && theater.getSeatsPerRow() != null) {
            theater.setTotalSeats(theater.getTotalRows() * theater.getSeatsPerRow());
        }
        theaterRepository.save(theater);
    }

    @Override
    public List<Theater> getAll() {
        return theaterRepository.findAll();
    }

    @Override
    public Theater getById(Long id) {
        return theaterRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        theaterRepository.deleteById(id);
    }
}
