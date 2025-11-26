package edu.guvi.moviebooking.controller;

import edu.guvi.moviebooking.entity.Show;
import edu.guvi.moviebooking.service.MovieService;
import edu.guvi.moviebooking.service.ShowService;
import edu.guvi.moviebooking.service.TheaterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/shows")
public class AdminShowController {

    private final ShowService showService;
    private final MovieService movieService;
    private final TheaterService theaterService;

    @GetMapping
    public String listShows(Model model) {
        model.addAttribute("shows", showService.getAll());
        return "admin/show-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("show", new Show());
        model.addAttribute("movies", movieService.getAll());
        model.addAttribute("theaters", theaterService.getAll());
        return "admin/add-show";
    }

    @PostMapping("/save")
    public String save(Show show) {
        showService.save(show);
        return "redirect:/admin/shows";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        showService.delete(id);
        return "redirect:/admin/shows";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("show", showService.getById(id));
        model.addAttribute("movies", movieService.getAll());
        model.addAttribute("theaters", theaterService.getAll());
        return "admin/edit-show";
    }

    @PostMapping("/update")
    public String update(Show show) {
        showService.save(show);
        return "redirect:/admin/shows";
    }

}
