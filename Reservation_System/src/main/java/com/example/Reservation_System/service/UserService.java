package com.example.Reservation_System.service;

import com.example.Reservation_System.model.User;

public interface UserService {
    User registerUser(User user);
    User login(String email, String password);
}
