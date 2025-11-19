package com.deep.intern.internship_api.config;

import com.deep.intern.internship_api.entity.User;
import com.deep.intern.internship_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("admin")) {

            User admin = new User(
                    null,
                    "admin",
                    "admin@example.com",
                    passwordEncoder.encode("Admin@123"),
                    "ROLE_ADMIN",
                    null
            );

            userRepository.save(admin);
            System.out.println("🌟 Admin User Created: username=admin password=Admin@123");
        }
    }
}
