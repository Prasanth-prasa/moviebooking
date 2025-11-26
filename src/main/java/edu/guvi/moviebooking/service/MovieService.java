package edu.guvi.moviebooking.service;

import edu.guvi.moviebooking.entity.Movie;
import java.util.List;

public interface MovieService {

    Movie save(Movie movie);
    List<Movie> getAll();
    Movie getById(Long id);
    void delete(Long id);
}
