package com.example.practiceusersession.respository;

import com.example.practiceusersession.model.UserPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository2 extends JpaRepository<UserPrincipal, Long> {
}
