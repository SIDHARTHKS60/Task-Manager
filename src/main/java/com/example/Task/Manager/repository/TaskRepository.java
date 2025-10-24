package com.example.Task.Manager.repository;

import com.example.Task.Manager.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByUsername(String username);
    Optional<Task> findByTaskId(int taskId);
    Task findTopByOrderByTaskIdDesc();
}
