package edu.guvi.moviebooking.service;

import edu.guvi.moviebooking.entity.Theater;
import java.util.List;

public interface TheaterService {
    void save(Theater theater);
    List<Theater> getAll();
    Theater getById(Long id);
    void delete(Long id);
}
