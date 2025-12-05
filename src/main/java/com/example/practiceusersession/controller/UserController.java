package com.example.practiceusersession.controller;

import com.example.practiceusersession.repository.UserRepository2;
import com.example.practiceusersession.service.UserService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.practiceusersession.model.UserPrincipal;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository2 repository;

    @Autowired
    private UserService2 userService2;

    @PostMapping("/save")
    public UserPrincipal saveUser(@RequestBody UserPrincipal user) {
        return userService2.save(user);
    }

    // 2. Get All Users
    @GetMapping("/all")
    public List<UserPrincipal> getAllUsers() {
        return userService2.getAllUsers();
    }

    // 3. Login
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password) {

        // Find user by email
        UserPrincipal user = repository.findByEmail(email);

        if (user == null) {
            return "Invalid Email!";
        }

        if (!user.getPassword().equals(password)) {
            return "Incorrect Password!";
        }

        return "Login Successful!";
    }
}
