package com.example.Task.Manager.repository;

import com.example.Task.Manager.model.AppUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepo;

    @Test
    @DisplayName("Should save a new user and retrieve it by username")
    void testSaveAndfindByUsername() {
        AppUser user1=new AppUser("Aaaa AA","Aaaaa@1111");
        userRepo.save(user1);

        AppUser foundUser = userRepo.findByUsername("Aaaa AA");
        assertNotNull(foundUser);
        assertEquals("Aaaa AA", foundUser.getUsername());
    }

   @Test
   @DisplayName("Should check if user present or not by username")
    void existsByUsername() {
       AppUser user2=new AppUser("Bbbb BB","Bbbbb@2222");
       userRepo.save(user2);

       Boolean found=userRepo.existsByUsername("Bbbb BB");
       assertTrue(found);
    }
}
