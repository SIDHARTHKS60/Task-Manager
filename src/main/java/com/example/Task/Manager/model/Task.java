package com.example.Task.Manager.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;


@Document(collection = "tasks")
public class Task {

    @Id
    private String id;

    private int taskId;
    private String title;
    private String description;
    private boolean completed;
    private String username;

    public Task(int taskId,String title, String description, String username, boolean completed) {
        this.taskId=taskId;
        this.title = title;
        this.description = description;
        this.username = username;
        this.completed = completed;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public Task() {

    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public int getTaskId() { return taskId; }
    public void setTaskId(int taskId) { this.taskId = taskId; }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;}

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Setter
    @Getter
    private Instant createdAt;
    @Setter
    @Getter
    private Instant updatedAt;
}
