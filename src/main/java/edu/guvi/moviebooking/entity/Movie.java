package edu.guvi.moviebooking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String genre;
    private String duration;
    private String language;

    @Column(length = 1000)
    private String description;
    private String rating; 

    private String poster;
}
