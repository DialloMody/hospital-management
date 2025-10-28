package com.hospitalmanagement.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * Service responsable de la génération, de la validation et de l’extraction des informations des tokens JWT.
 *
 * Il gère :
 * - la création du token à partir d’un utilisateur authentifié ;
 * - la vérification de sa validité et de sa date d’expiration ;
 * - l’extraction du nom d’utilisateur et d’autres informations contenues dans le token.
 */
@Service
public class JwtService {

    // Clé secrète utilisée pour signer les tokens (à sécuriser via des variables d’environnement en production)
    private static final String SECRET_KEY = "4A614E645267556B58703273357638792F423F4528482B4D6251655468576D5A";

    /** Génère un token JWT valide pour un utilisateur donné. */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // expire après 24h
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /** Extrait le nom d’utilisateur (subject) contenu dans le token. */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /** Vérifie que le token correspond bien à l’utilisateur et qu’il n’est pas expiré. */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /** Vérifie si le token est expiré. */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /** Extrait la date d’expiration du token. */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /** Extrait une information (claim) spécifique du token. */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /** Récupère tous les claims (informations) stockés dans le token. */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /** Retourne la clé de signature dérivée de la clé secrète (encodée en Base64). */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
