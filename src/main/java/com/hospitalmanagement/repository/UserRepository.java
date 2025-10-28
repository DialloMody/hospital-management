package com.hospitalmanagement.repository;

import com.hospitalmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository d’accès aux données pour l’entité {@link User}.
 *
 * <p>Cette interface hérite de {@link JpaRepository}, ce qui permet à Spring Data JPA
 * de générer automatiquement les méthodes CRUD (Create, Read, Update, Delete)
 * sans implémentation manuelle.</p>
 *
 * <p>Elle fournit également des méthodes personnalisées pour la recherche d’un utilisateur
 * par son nom d’utilisateur ou par son adresse e-mail, ainsi qu’une méthode
 * pour vérifier l’existence d’un e-mail avant l’enregistrement.</p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Recherche un utilisateur par son nom d’utilisateur.
     *
     * @param username le nom d’utilisateur à rechercher
     * @return un {@link Optional} contenant l’utilisateur s’il existe, sinon vide
     */
    Optional<User> findByUsername(String username);

    /**
     * Recherche un utilisateur par son adresse e-mail.
     *
     * @param email l’adresse e-mail à rechercher
     * @return un {@link Optional} contenant l’utilisateur s’il existe, sinon vide
     */
    Optional<User> findByEmail(String email);

    /**
     * Vérifie si un utilisateur existe déjà avec une adresse e-mail donnée.
     *
     * @param email l’adresse e-mail à vérifier
     * @return {@code true} si un utilisateur existe avec cet e-mail, sinon {@code false}
     */
    boolean existsByEmail(String email);
}
