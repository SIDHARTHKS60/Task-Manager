package com.example.Task.Manager.repository;

import com.example.Task.Manager.model.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<AppUser,String> {
    AppUser findByUsername(String username);
    boolean existsByUsername(String username);
}
