package com.deep.intern.internship_api.repository;

import com.deep.intern.internship_api.entity.Task;
import com.deep.intern.internship_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByOwner(User owner);
}
