package com.example.Task.Manager.service;

import com.example.Task.Manager.model.Task;
import com.example.Task.Manager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository repo;

    public TaskService(TaskRepository repo){
        this.repo=repo;
    }

    public Task create(String username, Task task){
         task.setId(null);
         task.setUserId(username);
         task.setCompleted(false);
         task.setCreatedAt(Instant.now());
         task.setUpdatedAt(Instant.now());
         return repo.save(task);
    }

    public List<Task> list(String username){
        return repo.findByUserId(username);
    }

    public Task getByIdForUser(String username, String id){
        Task t= repo.findById(id).orElseThrow(()-> new RuntimeException("Task is not found"));
        if(!t.getUserId().equals(username)) throw new RuntimeException("Forbidden");
        return t;
    }

    public Task update(String username, String id, Task payload) {
        Task t = getByIdForUser(username, id);
        t.setTitle(payload.getTitle());
        t.setDescription(payload.getDescription());
        t.setCompleted(payload.isCompleted());
        t.setUpdatedAt(Instant.now());
        return repo.save(t);
    }

    public void delete(String username, String id) {
        Task t = getByIdForUser(username, id);
        repo.delete(t);
    }


}
