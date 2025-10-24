package com.example.Task.Manager.controller;


import com.example.Task.Manager.model.Task;
import com.example.Task.Manager.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@AuthenticationPrincipal UserDetails ud, @RequestBody Task task) {
        return service.create(ud.getUsername(), task);
    }

    @GetMapping
    public List<Task> list(@AuthenticationPrincipal UserDetails ud) {
        return service.listTasks(ud.getUsername());
    }

    @GetMapping("/{taskId}")
    public Task get(@AuthenticationPrincipal UserDetails ud, @PathVariable int taskId) {
        return service.getTaskById(taskId);
    }

    @PutMapping("/{taskId}")
    public Task update(@PathVariable int taskId,
                       @RequestBody Task task) {
        return service.update(taskId, task);
    }

    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete( @PathVariable int taskId) {
        service.delete(taskId);
    }
}