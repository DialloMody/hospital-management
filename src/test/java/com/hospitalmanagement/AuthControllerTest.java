package com.hospitalmanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospitalmanagement.model.Role;
import com.hospitalmanagement.security.dto.LoginRequest;
import com.hospitalmanagement.security.dto.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // ✅ désactive les filtres de sécurité pour MockMvc
public class AuthControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp()
    {
        // Données d’inscription
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("john_doe");
        registerRequest.setEmail("johndoe@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setRoles(Set.of(Role.PATIENT));

        // Données de connexion
        loginRequest = new LoginRequest();
        loginRequest.setEmail("johndoe@example.com"); // ✅ correction ici
        loginRequest.setPassword("password123");
    }

    @Test
    void testRegisterUser() throws Exception
    {
        //  Inscription
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        //  Connexion
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }
}

