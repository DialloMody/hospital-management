package com.hospitalmanagement.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Représente la réponse renvoyée après une authentification réussie.
 * Contient uniquement le token JWT généré pour l’utilisateur connecté.
 */
@Data
@AllArgsConstructor
public class AuthResponse {
    /** Le token JWT attribué à l’utilisateur après connexion ou inscription. */
    private String token;
}
