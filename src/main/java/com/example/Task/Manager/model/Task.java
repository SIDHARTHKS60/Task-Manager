package com.example.Task.Manager.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;


@Document(collection = "tasks")
public class Task {
    @Id
    private String id;
    private String title;
    private String description;
    private boolean completed;
    private String username;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    @Setter@Getter
    private Instant createdAt;
    @Setter@Getter
    private Instant updatedAt;
}
