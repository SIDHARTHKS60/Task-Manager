package com.example.Task.Manager.repository;

import com.example.Task.Manager.model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.*;


@DataMongoTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepo;

    @Test
    @DisplayName("Should find the task for the user name")
    void testSaveAndfindTaskByUsername() {
        Task task1 =new Task(1, "Python","Learn python","Aaaa AA",false);
        taskRepo.save(task1);

        assertNotNull(task1.getId(), "MongoDB should auto-generate ID");
        assertEquals("Aaaa AA", task1.getUsername());
        assertFalse(task1.isCompleted());

    }

}

