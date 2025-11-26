package edu.guvi.moviebooking.service;

import edu.guvi.moviebooking.entity.User;

public interface UserService {
    User register(User user); 
    User login(String email, String password);
    String getUserEmail(Long userId);
    String getUserEmailByUsername(String username);
    Long getUserIdByEmail(String email);
    Long getLoggedInUserId();




}
