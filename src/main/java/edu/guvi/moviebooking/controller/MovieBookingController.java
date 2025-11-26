package edu.guvi.moviebooking.controller;

import edu.guvi.moviebooking.entity.Movie;
import edu.guvi.moviebooking.service.MovieService;
import edu.guvi.moviebooking.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class MovieBookingController {

    private final MovieService movieService;
    private final ShowService showService;

    @GetMapping("/movie/{id}")
    public String showMovie(@PathVariable Long id, Model model) {

        Movie movie = movieService.getById(id);

        model.addAttribute("movie", movie);
        model.addAttribute("shows", showService.getByMovieId(id));

        return "movie-show"; // must match movie-show.html
    }
}
