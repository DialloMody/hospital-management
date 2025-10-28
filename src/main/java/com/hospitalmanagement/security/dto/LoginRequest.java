package com.hospitalmanagement.security.dto;

import lombok.Data;

/**
 * Requête utilisée lors de la connexion d’un utilisateur.
 * Contient les identifiants nécessaires pour l’authentification.
 */
@Data
public class LoginRequest {

    /** Adresse e-mail de l’utilisateur. */
    private String email;

    /** Mot de passe associé à l’e-mail. */
    private String password;
}
