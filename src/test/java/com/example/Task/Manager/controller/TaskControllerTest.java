package com.example.Task.Manager.controller;

import com.example.Task.Manager.model.Task;
import com.example.Task.Manager.security.JwtFilter;
import com.example.Task.Manager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private JwtFilter jwtFilter;

//    private Task sampleTask;

  /*  @BeforeEach
    void setUp() {
        sampleTask = new Task(1, "Test task", "Testing", "Aaaa AA", false);
    }*/

    @Test
    @WithMockUser(username="Aaaa AA",roles = {"USER"})
    void testCreateTask() throws Exception {
        // Arrange
        Task sampleTask = new Task(
                1,
                "Test task",
                "Testing description",
                "Aaaa AA",
                true

        );

        when(taskService.create(anyString(), any(Task.class))).thenReturn(sampleTask);

        // Act + Assert
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Test task\",\"description\":\"Testing description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test task"))
                .andExpect(jsonPath("$.username").value("Aaaa AA"))
                .andExpect(jsonPath("$.description").value("Testing description"))
                .andExpect(jsonPath("$.completed").value(true));
    }

  @Test
  @WithMockUser(username="Aaaa AA",roles = {"USER"})
    void getTasksforUser() throws Exception {
        List<Task> tasks= Arrays.asList(
                new Task(1,"Task1","task 1 descript","Aaaa AA",true),
                new Task(2,"task2","Task 2 descript","Aaaa AA",false),
                new Task(2,"task3","Task 2 descript","Bbbb AA",true)
      );

      Mockito.when(taskService.listTasks("Aaaa AA")).thenReturn(tasks);

      mockMvc.perform(get("/tasks"))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$[0].taskId").value(1))
              .andExpect(jsonPath("$[1].title").value("task2"))
              .andExpect(jsonPath("$[2].title").value("task3"));


    }

     @Test
    void getTaskById() throws Exception {
        Task task=new Task(4,"task 4", "4th task","Cccc CC", true);
        Mockito.when(taskService.getTaskById(4)).thenReturn(task);

        mockMvc.perform(get("/tasks/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(4))
                .andExpect(jsonPath("$.title").value("task 4"));
    }

    @Test
    @WithMockUser(username = "Dddd DD")
    void testUpdateTask() throws Exception {
        Task updatedTask = new Task(5, "Updated Task", "Completed","Dddd DD",false);
        Mockito.when(taskService.update(eq(5),any(Task.class))).thenReturn(updatedTask);

        mockMvc.perform(put("/tasks/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                        "taskId":5,
                          "title": "Updated Task",
                          "description": "Completed",
                          "username":"Dddd DD",
                          "completed": true
                        }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.description").value("Completed"))
                .andExpect(jsonPath("$.completed").value(false));

        verify(taskService).update(eq(5),any(Task.class));
    }

    // ✅ DELETE TASK
    @Test
    @WithMockUser(username = "john")
    void testDeleteTask() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isOk());

        Mockito.verify(taskService).delete(1);
    }
}