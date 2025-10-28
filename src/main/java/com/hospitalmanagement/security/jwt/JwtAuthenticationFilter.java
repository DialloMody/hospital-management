package com.hospitalmanagement.security.jwt;

import com.hospitalmanagement.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtre d’authentification JWT exécuté une fois par requête.
 *
 * Il intercepte chaque requête HTTP, extrait le token JWT du header "Authorization",
 * vérifie sa validité et, si valide, authentifie l’utilisateur dans le contexte de sécurité Spring.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    /**
     * Méthode principale exécutée à chaque requête :
     * - extrait le JWT du header Authorization ;
     * - valide le token ;
     * - charge les informations de l’utilisateur ;
     * - met à jour le contexte de sécurité si tout est valide.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // Vérifie la présence et le format du header Authorization
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extrait le token JWT du header
        jwt = authHeader.substring(7);
        username = jwtService.extractUsername(jwt);

        // Vérifie que l’utilisateur n’est pas déjà authentifié
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Valide le token avant d’établir l’authentification
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Met à jour le contexte de sécurité Spring
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Passe la requête au filtre suivant
        filterChain.doFilter(request, response);
    }
}
