package com.example.Reservation_System.controller;

import com.example.Reservation_System.model.User;
import com.example.Reservation_System.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000") // ✅ allow frontend React
public class UserController {

    @Autowired
    private UserService userService;

    // ✅ Register new user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        User savedUser = userService.registerUser(user);
        if (savedUser == null) {
            return ResponseEntity.badRequest().body("❌ User already exists with email: " + user.getEmail());
        }
        return ResponseEntity.ok("✅ User registered successfully!");
    }

    // ✅ Login user
    @PostMapping("/login")
 public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
    User user = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
    if (user == null) {
        return ResponseEntity.status(401).body("{\"message\":\"❌ Invalid email or password\"}");
    }
    // Return JSON object
    return ResponseEntity.ok(user); // user contains name & email
}

}
