package com.example.practiceusersession.service;

import com.example.practiceusersession.model.UserPrincipal;
import com.example.practiceusersession.repository.UserRepository2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository2 repository;   // UPDATED ✔

    @InjectMocks
    private UserService2 service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUser() {
        System.out.println("testSaveUser is running ");

        UserPrincipal user = new UserPrincipal();
        user.setEmail("test@gmail.com");
        user.setPassword("123");

        when(repository.save(user)).thenReturn(user);

        UserPrincipal saved = repository.save(user);

        assertNotNull(saved);
        assertEquals("test@gmail.com", saved.getEmail());
        verify(repository, times(1)).save(user);
    }


}
