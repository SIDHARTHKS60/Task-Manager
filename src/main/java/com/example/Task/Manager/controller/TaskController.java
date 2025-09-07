package com.example.Task.Manager.controller;


import com.example.Task.Manager.model.Task;
import com.example.Task.Manager.service.TaskService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) { this.service = service; }

    @PostMapping
    public Task create(@AuthenticationPrincipal UserDetails ud, @RequestBody Task task) {
        return service.create(ud.getUsername(), task);
    }

    @GetMapping
    public List<Task> list(@AuthenticationPrincipal UserDetails ud) {
        return service.list(ud.getUsername());
    }

    @GetMapping("/{id}")
    public Task get(@AuthenticationPrincipal UserDetails ud, @PathVariable String id) {
        return service.getByIdForUser(ud.getUsername(), id);
    }

    @PutMapping("/{id}")
    public Task update(@AuthenticationPrincipal UserDetails ud,
                       @PathVariable String id,
                       @RequestBody Task task) {
        return service.update(ud.getUsername(), id, task);
    }

    @DeleteMapping("/{id}")
    public void delete(@AuthenticationPrincipal UserDetails ud, @PathVariable String id) {
        service.delete(ud.getUsername(), id);
    }
}