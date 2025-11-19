package com.deep.intern.internship_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private boolean completed = false;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    private Instant createdAt = Instant.now();

    private Instant updatedAt;
}
