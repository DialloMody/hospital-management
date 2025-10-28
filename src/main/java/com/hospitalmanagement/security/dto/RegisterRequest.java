package com.hospitalmanagement.security.dto;

import com.hospitalmanagement.model.Role;
import lombok.Data;
import java.util.Set;

/**
 * Requête utilisée lors de l'inscription d'un nouvel utilisateur.
 * Contient les informations nécessaires pour créer un compte.
 */
@Data
public class RegisterRequest {

    /** Nom d’utilisateur choisi par la personne à inscrire. */
    private String username;

    /** Adresse e-mail utilisée pour l’authentification. */
    private String email;

    /** Mot de passe de l’utilisateur (sera encodé avant stockage). */
    private String password;

    /** Ensemble des rôles attribués à l’utilisateur. */
    private Set<Role> roles;
}
