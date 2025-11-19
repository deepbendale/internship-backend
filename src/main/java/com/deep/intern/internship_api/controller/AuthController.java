package com.deep.intern.internship_api.controller;

import com.deep.intern.internship_api.dto.AuthRequest;
import com.deep.intern.internship_api.dto.AuthResponse;
import com.deep.intern.internship_api.dto.RegisterDto;
import com.deep.intern.internship_api.entity.User;
import com.deep.intern.internship_api.repository.UserRepository;
import com.deep.intern.internship_api.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto dto) {

        if (userRepository.existsByUsername(dto.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        User user = new User(
                null,
                dto.getUsername(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()),
                "ROLE_USER",
                null
        );

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

        return ResponseEntity.ok(new AuthResponse(token));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {

        User user = userRepository.findByUsername(req.getUsername())
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

        return ResponseEntity.ok(new AuthResponse(token));
    }
}
