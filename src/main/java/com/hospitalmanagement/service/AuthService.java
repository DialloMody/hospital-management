package com.hospitalmanagement.service;

import com.hospitalmanagement.model.User;
import com.hospitalmanagement.repository.UserRepository;
import com.hospitalmanagement.security.dto.AuthResponse;
import com.hospitalmanagement.security.dto.LoginRequest;
import com.hospitalmanagement.security.dto.RegisterRequest;
import com.hospitalmanagement.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service gérant l'authentification et l'enregistrement des utilisateurs.
 *
 * Coordonne les interactions entre la base de données, la gestion des mots de passe,
 * le système d’authentification Spring Security et la génération de JWT.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Enregistre un nouvel utilisateur, chiffre son mot de passe,
     * sauvegarde ses données et génère un token JWT.
     */
    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(request.getRoles())
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    /**
     * Authentifie un utilisateur via son email et mot de passe,
     * puis renvoie un token JWT si la connexion est valide.
     */
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur pas trouvé"));

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}
