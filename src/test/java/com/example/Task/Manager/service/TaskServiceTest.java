package com.example.Task.Manager.service;


import com.example.Task.Manager.model.Task;
import com.example.Task.Manager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepo;

    @InjectMocks
    private TaskService taskService;

    private Task sampleTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleTask = new Task(1, "Python","Learn Python","Cccc CC",false);

        // Mock SecurityContext
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("Cccc CC");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("Should create a new task for a user")
    void shouldCreateTaskSuccessfully() {
        when(taskRepo.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        Task result = taskService.create("Cccc CC",sampleTask);

        assertNotNull(result);
        assertEquals("Cccc CC", result.getUsername());
        assertFalse(result.isCompleted());
        assertNotNull(result.getCreatedAt());
        verify(taskRepo, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Should list all tasks for user")
    void testListTasks() {
        when(taskRepo.findByUsername("Cccc CC")).thenReturn(List.of(sampleTask));

        List<Task> tasks = taskService.listTasks("Cccc CC");

        assertEquals(1, tasks.size());
        assertEquals("Cccc CC", tasks.get(0).getUsername());
    }

    @Test
    @DisplayName("Should get task by id for correct user")
    void testGetByIdForUser() {
        when(taskRepo.findByTaskId(1)).thenReturn(Optional.of(sampleTask));

        Task t = taskService.getTaskById( 1);

        assertEquals("Cccc CC", t.getUsername());
    }

    @Test
    @DisplayName("Should throw when user tries to access another user's task")
    void testGetByIdForbidden() {
        sampleTask.setUsername("otherUser");
        when(taskRepo.findByTaskId(1)).thenReturn(Optional.of(sampleTask));

        assertThrows(RuntimeException.class, () -> taskService.getTaskById(1));
    }

    @Test
    @DisplayName("Should update a task for the correct user")
    void testUpdateTask() {
        when(taskRepo.findByTaskId(1)).thenReturn(Optional.of(sampleTask));
        when(taskRepo.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        Task payload = new Task(1, "Updated", "Desc", "Aaaa AA", true);
        Task updated = taskService.update(1,payload);

        assertEquals("Updated", updated.getTitle());
        assertTrue(updated.isCompleted());
        verify(taskRepo, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Should delete task for user")
    void testDeleteTask() {
        when(taskRepo.findByTaskId(1)).thenReturn(Optional.of(sampleTask));
        doNothing().when(taskRepo).delete(sampleTask);

        taskService.delete(1);

        verify(taskRepo, times(1)).delete(sampleTask);
    }
}
