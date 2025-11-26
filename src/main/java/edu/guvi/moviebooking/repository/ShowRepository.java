package edu.guvi.moviebooking.repository;

import edu.guvi.moviebooking.entity.Show;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowRepository extends JpaRepository<Show, Long> {

    List<Show> findByMovieId(Long movieId);

}
