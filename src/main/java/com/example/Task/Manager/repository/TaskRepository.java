package com.example.Task.Manager.repository;

import com.example.Task.Manager.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaskRepository extends MongoRepository<Task,String> {
 List<Task> findByUserId(String userId);
}
