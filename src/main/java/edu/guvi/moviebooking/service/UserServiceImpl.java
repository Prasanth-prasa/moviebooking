package edu.guvi.moviebooking.service;

import edu.guvi.moviebooking.entity.User;
import edu.guvi.moviebooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public User register(User user) {
        user.setRole("USER");
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(u -> encoder.matches(password, u.getPassword()))
                .orElse(null);
    }

    @Override
public String getUserEmail(Long userId) {
    return userRepository.findById(userId)
            .map(User::getEmail)
            .orElse(null);
}

@Override
public String getUserEmailByUsername(String username) {
    return userRepository.findByEmail(username)
            .map(User::getEmail)
            .orElse(null);
}

@Override
public Long getUserIdByEmail(String email) {
    return userRepository.findByEmail(email)
            .map(User::getId)
            .orElse(null);
}


@Override
public Long getLoggedInUserId() {
    String username = SecurityContextHolder.getContext()
            .getAuthentication()
            .getName();

    return userRepository.findByEmail(username)
            .map(User::getId)
            .orElse(null);
}

}
