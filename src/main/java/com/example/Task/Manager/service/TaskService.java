package com.example.Task.Manager.service;

import com.example.Task.Manager.model.Task;
import com.example.Task.Manager.repository.TaskRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
         task.setTaskId(getNextTaskId());
         task.setUsername(username);
         task.setCompleted(false);
         task.setCreatedAt(Instant.now());
         task.setUpdatedAt(Instant.now());
         return repo.save(task);
    }

    private int getNextTaskId() {
        // find the task with the largest taskId and +1
        Task lastTask = repo.findTopByOrderByTaskIdDesc();
        return (lastTask == null) ? 1 : lastTask.getTaskId() + 1;
    }

    public List<Task> listTasks(String username){
        return repo.findByUsername(username);
    }

    public Task getTaskById(int taskId) {
        // Get the logged-in user's username
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        // Find task
        Task t = repo.findByTaskId(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Ownership check
        if (!t.getUsername().equals(currentUsername)) {
            throw new RuntimeException("Forbidden");
        }

        return t;
    }

    public Task update(int taskId, Task payload) {
        Task t = getTaskById(taskId);
        t.setTitle(payload.getTitle());
        t.setDescription(payload.getDescription());
        t.setCompleted(payload.isCompleted());
        t.setUpdatedAt(Instant.now());
        return repo.save(t);
    }

    public void delete(int taskId) {
        Task t = getTaskById(taskId);
        repo.delete(t);
    }


}
