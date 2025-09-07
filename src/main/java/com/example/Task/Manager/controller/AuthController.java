package com.example.Task.Manager.controller;

import com.example.Task.Manager.dto.*;
import com.example.Task.Manager.model.AppUser;
import com.example.Task.Manager.repository.UserRepository;
import com.example.Task.Manager.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;

    public AuthController(UserRepository userRepo, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
        this.encoder = passwordEncoder;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest req) {
        if (req.getUsername() == null || req.getPassword() == null) {
            return "username and password required";
        }
        if (userRepo.existsByUsername(req.getUsername())) {
            return "Username already taken";
        }
        AppUser u = new AppUser(req.getUsername(), encoder.encode(req.getPassword()));
        userRepo.save(u);
        return "Registered";
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest req) {
        AppUser u = userRepo.findByUsername(req.getUsername());
        if (u != null && encoder.matches(req.getPassword(), u.getPassword())) {
            String token = jwtUtil.generateToken(u.getUsername());
            return new AuthResponse(token);
        }
        throw new RuntimeException("Invalid credentials");
    }
}
