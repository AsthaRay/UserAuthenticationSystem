package com.example.practiceusersession.service;

import com.example.practiceusersession.model.UserPrincipal;
import com.example.practiceusersession.repository.UserRepository2;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserService2 {
    @Autowired
    UserRepository2 repository;

    public UserPrincipal save(UserPrincipal user) {

        return repository.save(user);
    }

    public List<UserPrincipal> getAllUsers() {
        return repository.findAll();
    }



    public boolean login(String email, String password) {

        // 1. Get user by username
        UserPrincipal user = repository.findByEmail(email);

        if (user == null) {
            return false;
        }


        return user.getPassword().equals(password);
    }
}
