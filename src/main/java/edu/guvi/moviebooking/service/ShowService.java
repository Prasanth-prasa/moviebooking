package edu.guvi.moviebooking.service;

import edu.guvi.moviebooking.entity.Show;
import java.util.List;

public interface ShowService {

    void save(Show show);

    List<Show> getAll();

    Show getById(Long id);

    void delete(Long id);


    List<Show> getByMovieId(Long movieId);

}
