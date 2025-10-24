package com.example.Task.Manager.integration;

import com.example.Task.Manager.model.Task;
import com.example.Task.Manager.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private ObjectMapper objectMapper;

    private static String token;

    @BeforeAll
    static void intro() {
        System.out.println("🔐 Starting full TaskController + JWT integration tests...");
    }

    /*void cleanDatabase() {
        taskRepo.deleteAll();
        userRepository.deleteAll();
    }*/



    @Test
    @Order(1)
    void registerUser() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"testuser\", \"password\": \"testpass\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void loginUserAndStoreToken() throws Exception {
        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"testuser\", \"password\": \"testpass\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        token = objectMapper.readTree(responseBody).get("token").asText();
        assertThat(token).isNotNull();
    }

    @Test
    @Order(3)
    void createTask() throws Exception {
        Task task = new Task();
        task.setTitle("Integration Task 1");
        task.setDescription("Testing task creation");
        task.setCompleted(false);

        mockMvc.perform(post("/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Integration Task 1"))
                .andExpect(jsonPath("$.description").value("Testing task creation"));
    }


    @Test
    @Order(4)
    void getAllTasksForUser() throws Exception {
        mockMvc.perform(get("/tasks")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Integration Task 1"));
    }

    @Test
    @Order(5)
    void updateTask() throws Exception {
        List<Task> tasks = taskRepo.findAll();
        assertThat(tasks).isNotEmpty();
        int id = tasks.getFirst().getTaskId();

        Task updated = new Task();
        updated.setTitle("Updated Title");
        updated.setDescription("Updated Description");
        updated.setCompleted(true);

        mockMvc.perform(put("/tasks/" + id)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    @Order(6)
    void deleteTask() throws Exception {
        List<Task> tasks = taskRepo.findAll();
        assertThat(tasks).isNotEmpty();
        int taskId = tasks.get(0).getTaskId();

        mockMvc.perform(delete("/tasks/" + taskId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        assertThat(taskRepo.findByTaskId(taskId)).isEmpty();
    }

    @Test
    @Order(7)
    void shouldReturnUnauthorizedWithoutToken() throws Exception {
        mockMvc.perform(get("/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @AfterAll
    static void done() {
        System.out.println("✅ All integration tests completed successfully!");
    }
}
