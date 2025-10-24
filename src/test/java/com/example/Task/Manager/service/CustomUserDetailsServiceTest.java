package com.example.Task.Manager.service;

import com.example.Task.Manager.model.AppUser;
import com.example.Task.Manager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private AppUser sampleUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleUser = new AppUser("Aaaa AA", "Aaaa@111"); 
        // according to your entity constructor
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        // Arrange
        when(userRepository.findByUsername("Aaaa AA")).thenReturn(sampleUser);

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("Aaaa AA");

        // Assert
        assertNotNull(userDetails);
        assertEquals("Aaaa AA", userDetails.getUsername());
        assertEquals("Aaaa@111", userDetails.getPassword());
        verify(userRepository, times(1)).findByUsername("Aaaa AA");
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        when(userRepository.findByUsername("unknown_user")).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("unknown_user");
        });
        verify(userRepository, times(1)).findByUsername("unknown_user");
    }
}
