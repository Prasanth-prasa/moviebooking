package edu.guvi.moviebooking.controller;

import edu.guvi.moviebooking.entity.Movie;
import edu.guvi.moviebooking.service.CloudinaryService;
import edu.guvi.moviebooking.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/movies")
public class AdminMovieController {

    private final MovieService movieService;
    private final CloudinaryService cloudinaryService;

    @GetMapping
    public String listMovies(Model model) {
        model.addAttribute("movies", movieService.getAll());
        return "admin/movie-list";
    }

    @GetMapping("/add")
    public String addMovieForm() {
        return "admin/add-movie";
    }

    @PostMapping("/save")
    public String saveMovie(Movie movie, @RequestParam("posterFile") MultipartFile file) throws IOException {

        if (!file.isEmpty()) {
            movie.setPoster(cloudinaryService.upload(file));
        }

        movieService.save(movie);
        return "redirect:/admin/movies?saved";
    }

    @GetMapping("/delete/{id}")
    public String deleteMovie(@PathVariable Long id) {
        movieService.delete(id);
        return "redirect:/admin/movies?deleted";
    }

    @PostMapping("/update")
    public String updateMovie(Movie movie, @RequestParam("posterFile") MultipartFile file) throws Exception {

        // If new file added -> update in cloudinary
        if (!file.isEmpty()) {
            String url = cloudinaryService.upload(file);
            movie.setPoster(url);
        } else {
            // Keep old poster (fetch from db)
            movie.setPoster(movieService.getById(movie.getId()).getPoster());
        }

        movieService.save(movie);
        return "redirect:/admin/movies?updated=true";
    }

    @GetMapping("/edit/{id}")
    public String editMovie(@PathVariable Long id, Model model) {
        model.addAttribute("movie", movieService.getById(id));
        return "admin/admin-edit-movie"; // 👈 because file is inside admin folder
    }

}
