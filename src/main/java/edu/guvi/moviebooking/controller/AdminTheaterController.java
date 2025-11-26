package edu.guvi.moviebooking.controller;

import edu.guvi.moviebooking.entity.Theater;
import edu.guvi.moviebooking.service.TheaterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/theaters")
public class AdminTheaterController {

    private final TheaterService theaterService;

    @GetMapping
    public String listTheaters(Model model) {
        model.addAttribute("theaters", theaterService.getAll());
        return "admin/theater-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("theater", new Theater());
        return "admin/add-theater";
    }

    @PostMapping("/save")
    public String save(Theater theater) {
        theaterService.save(theater);
        return "redirect:/admin/theaters";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("theater", theaterService.getById(id));
        return "admin/add-theater";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        theaterService.delete(id);
        return "redirect:/admin/theaters";
    }
}
