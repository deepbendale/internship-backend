package com.deep.intern.internship_api.controller;

import com.deep.intern.internship_api.entity.Task;
import com.deep.intern.internship_api.entity.User;
import com.deep.intern.internship_api.repository.TaskRepository;
import com.deep.intern.internship_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getTasks(Authentication auth) {
        String username = auth.getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        // Admin gets ALL tasks
        if (user.getRole().equals("ROLE_ADMIN")) {
            return ResponseEntity.ok(taskRepository.findAll());
        }

        // User only gets their own tasks
        return ResponseEntity.ok(taskRepository.findByOwner(user));
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody Task task, Authentication auth) {
        String username = auth.getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        task.setOwner(user);
        task.setCreatedAt(Instant.now());
        return ResponseEntity.ok(taskRepository.save(task));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(@PathVariable Long id, Authentication auth) {
        Task task = taskRepository.findById(id).orElseThrow();

        String username = auth.getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        if (!user.getRole().equals("ROLE_ADMIN") &&
                !task.getOwner().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Forbidden");
        }

        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(
            @PathVariable Long id,
            @RequestBody Task request,
            Authentication auth) {

        Task task = taskRepository.findById(id).orElseThrow();

        String username = auth.getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        if (!user.getRole().equals("ROLE_ADMIN") &&
                !task.getOwner().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Forbidden");
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(request.isCompleted());
        task.setUpdatedAt(Instant.now());

        return ResponseEntity.ok(taskRepository.save(task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id, Authentication auth) {
        Task task = taskRepository.findById(id).orElseThrow();

        String username = auth.getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        if (!user.getRole().equals("ROLE_ADMIN") &&
                !task.getOwner().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Forbidden");
        }

        taskRepository.delete(task);
        return ResponseEntity.ok("Task deleted");
    }
}
