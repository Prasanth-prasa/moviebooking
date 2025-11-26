package edu.guvi.moviebooking.config;

import edu.guvi.moviebooking.entity.User;
import edu.guvi.moviebooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {

        if (userRepository.findByEmail("admin@movie.com").isEmpty()) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@movie.com");
            admin.setPassword(new BCryptPasswordEncoder().encode("admin123"));
            admin.setRole("ADMIN");

            userRepository.save(admin);
            System.out.println("✔ Admin Created: admin@movie.com | Password: admin123");
        } else {
            System.out.println("✔ Admin already exists. Skipping creation.");
        }
    }
}
