package com.example.practiceusersession.security;

import com.example.practiceusersession.model.User;
import com.example.practiceusersession.respository.UserRepository;
import com.example.practiceusersession.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        // These endpoints DO NOT need JWT
        return path.startsWith("/api/v1/register")
                || path.startsWith("/api/v1/login")
                || path.startsWith("/api/v1/otp");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String email = jwtUtil.extractEmail(token);
            if (email != null
                    && SecurityContextHolder.getContext().getAuthentication() == null
                    && jwtUtil.validateToken(token, email)) {

                Optional<User> optionalUser =userRepository.findByEmail(email);
                if (optionalUser.isPresent()){
                    String roleName = optionalUser.get().getRoles().get(0).getName();
                    System.out.println("your rolename is " + roleName);
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(roleName);
                    List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
                    grantedAuthorityList.add(grantedAuthority);
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    email, null, grantedAuthorityList);


                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
