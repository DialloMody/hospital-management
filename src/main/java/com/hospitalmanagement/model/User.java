package com.hospitalmanagement.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Représente un utilisateur du système hospitalier.
 * Implémente {@link UserDetails} pour l’intégration avec Spring Security.
 * Chaque utilisateur possède des rôles et des informations d’authentification.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users") // évite le conflit avec le mot réservé "user"
public class User implements UserDetails {

    /** Identifiant unique de l'utilisateur. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nom d'utilisateur (peut être distinct de l'email). */
    private String username;

    /** Adresse e-mail servant d’identifiant de connexion. */
    @Column(unique = true, nullable = false)
    private String email;

    /** Mot de passe encodé de l’utilisateur. */
    private String password;

    /** Ensemble des rôles attribués à l'utilisateur (ADMIN, MEDECIN, etc.). */
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    // ------------------------------------------------------------------------
    // Implémentation de l'interface UserDetails pour Spring Security
    // ------------------------------------------------------------------------

    /** Retourne les autorités (rôles) de l'utilisateur pour la sécurité Spring. */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }

    /** Retourne le mot de passe encodé. */
    @Override
    public String getPassword() {
        return password;
    }

    /** Utilise l'email comme identifiant principal pour la connexion. */
    @Override
    public String getUsername() {
        return email;
    }

    /** Indique si le compte est expiré (toujours actif ici). */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /** Indique si le compte est verrouillé (jamais dans ce cas). */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /** Indique si les identifiants sont expirés (jamais ici). */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /** Indique si le compte est activé (toujours actif). */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
