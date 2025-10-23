package com.hospitalmanagement.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil
{
    private static final String JWT_SECRET = "secret-key-123456789012345678901234567890";
    private static final long EXPIRATION_MS = 24 * 60 * 60 * 1000; // 24h

    private final Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());

    public String generateToken(String username)
    {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date( System.currentTimeMillis() + EXPIRATION_MS ))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token)
    {
        return parseClaims(token).getSubject();
    }

    public boolean validateToken(String token)
    {
        try {
            parseClaims(token);
            return true;
        }
        catch (JwtException |  IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
