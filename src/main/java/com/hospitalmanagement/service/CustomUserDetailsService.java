package com.hospitalmanagement.service;

import com.hospitalmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service responsable de charger un utilisateur depuis la base de données
 * pour l’authentification Spring Security.
 *
 * <p>Il utilise l'email comme identifiant de connexion, et retourne directement
 * l'entité {@code User} qui implémente {@code UserDetails}.</p>
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Recherche l'utilisateur par son email et le renvoie à Spring Security.
     *
     * @param email l'adresse e-mail utilisée pour se connecter
     * @return les détails de l'utilisateur pour l’authentification
     * @throws UsernameNotFoundException si aucun utilisateur n’est trouvé
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + email));
    }
}
