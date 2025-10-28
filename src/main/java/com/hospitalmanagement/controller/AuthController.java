package com.hospitalmanagement.security.controller;

import com.hospitalmanagement.security.dto.AuthResponse;
import com.hospitalmanagement.security.dto.LoginRequest;
import com.hospitalmanagement.security.dto.RegisterRequest;
import com.hospitalmanagement.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST responsable de la gestion de l’authentification et de l’inscription des utilisateurs.
 * Fournit des endpoints publics pour s’enregistrer et se connecter.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint d’inscription d’un nouvel utilisateur.
     * @param request les informations d’enregistrement (nom, e-mail, mot de passe, rôle)
     * @return un token JWT pour l’utilisateur enregistré
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * Endpoint de connexion d’un utilisateur existant.
     * @param request les identifiants de connexion (e-mail et mot de passe)
     * @return un token JWT si l’authentification réussit
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
