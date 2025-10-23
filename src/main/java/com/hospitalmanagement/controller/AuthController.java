package com.hospitalmanagement.controller;

import com.hospitalmanagement.model.Role;
import com.hospitalmanagement.model.User;
import com.hospitalmanagement.repository.UserRepository;
import com.hospitalmanagement.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController
{
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public String registerUser(@RequestBody Map<String, String> request)
    {
        User user = User.builder()
                .username(request.get("username"))
                .email(request.get("email"))
                .password(passwordEncoder.encode(request.get("password")))
                .roles(Set.of(Role.ADMIN))
                .build();

        userRepository.save(user);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request)
    {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.get("username"), request.get("password")));
        String token = jwtUtil.generateToken(request.get("username"));

        return Map.of("token", token);
    }
}
