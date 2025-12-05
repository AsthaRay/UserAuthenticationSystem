package com.example.practiceusersession.controller;

import com.example.practiceusersession.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo")
public class RoleDemoController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/admin-only")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminApi() {
        return "THIS IS ADMIN ONLY DATA";
    }

    @GetMapping("/user-only")
    @PreAuthorize("hasRole('USER')")
    public String userApi() {
        return "THIS IS USER ONLY DATA";
    }

    @GetMapping("/common")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public String commonApi() {
        return "THIS IS COMMON DATA FOR BOTH";
    }

    @GetMapping("/check-access")
    public ResponseEntity<String> checkAccess(
            @RequestParam String email,
            @RequestParam String type) {

        var user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        boolean isUser = user.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("ROLE_USER"));

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("ROLE_ADMIN"));

        // Check requested type
        if (type.equalsIgnoreCase("user") && isUser) {
            return ResponseEntity.ok("THIS IS USER ONLY DATA");
        }

        if (type.equalsIgnoreCase("admin") && isAdmin) {
            return ResponseEntity.ok("THIS IS ADMIN ONLY DATA");
        }

        // Not allowed
        return ResponseEntity.status(403).body("ACCESS DENIED");
    }
}
