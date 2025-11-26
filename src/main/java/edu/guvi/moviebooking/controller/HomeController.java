package edu.guvi.moviebooking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import edu.guvi.moviebooking.entity.Movie;
import edu.guvi.moviebooking.service.MovieService;
import edu.guvi.moviebooking.service.ShowService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MovieService movieService;
    private final ShowService showService;

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("movies", movieService.getAll());
        return "home";
    }

    @GetMapping("/admin/dashboard")
    public String admin() {
        return "admin/admin-dashboard";
    }

    @GetMapping("/shows/{movieId}")
    public String showTimes(@PathVariable Long movieId, Model model) {
        Movie movie = movieService.getById(movieId);
        model.addAttribute("movie", movie);
        model.addAttribute("shows", showService.getByMovieId(movieId));

        return "shows/list"; // <-- Correct path
    }
}
