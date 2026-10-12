package com.habit.tracker.controller;

import java.util.Map;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.habit.tracker.model.User;
import com.habit.tracker.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthController(UserRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User u) {
        if (repo.findByEmail(u.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already registered"));
        }
        u.setPassword(encoder.encode(u.getPassword()));
        return ResponseEntity.ok(repo.save(u));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        Optional<User> user = repo.findByEmail(body.get("email"));
        if (user.isPresent() && encoder.matches(body.get("password"), user.get().getPassword())) {
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.status(401).body(Map.of("message", "Invalid email or password"));
    }
}