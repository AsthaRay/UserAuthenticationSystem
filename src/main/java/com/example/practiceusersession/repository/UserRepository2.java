package com.example.practiceusersession.repository;

import com.example.practiceusersession.model.UserPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository2 extends JpaRepository<UserPrincipal,Integer> {

    UserPrincipal findByUsername(String username);
    UserPrincipal findByEmail(String mail);
}
