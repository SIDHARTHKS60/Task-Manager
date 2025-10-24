package com.example.Task.Manager.integration;


import com.example.Task.Manager.model.AppUser;
import com.example.Task.Manager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder encoder;

    @BeforeEach
    void setup(){
        userRepo.deleteAll();
    }

    @Test
    void testRegisterUser() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content("{\"username\":\"Aaaa AA\", \"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Registered"));


        // ✅ confirm saved in DB
//        assert(userRepo.findByUsername("Aaaa AA").isPresent());
      /*  AppUser savedUser=userRepo.findByUsername("Aaaa AA");
        assertNotNull(savedUser);*/
    }

    @Test
    void testLoginUser() throws Exception {
        // first, register a user manually
        AppUser user = new AppUser("Aaaa AA", encoder.encode("password123"));
        userRepo.save(user);

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content("{\"username\":\"Aaaa AA\", \"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());  // ✅ JSON body with token
    }

}
