package com.Unir.RelatosdePapel.User.utils;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtils {

    // En un entorno real, esta clave debe ser más segura y obtenida a través de variables de entorno o un gestor de secretos
    private static final String SECRET_KEY = "unir-supplies-secret-key-for-jwt-token-generation-and-validation-2025";
    private static final long ACCESS_TOKEN_VALIDITY = 5 * 60 * 1000; // 5 minutos
    private final SecretKey key;

    public JwtUtils() {
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                            .verifyWith(key)
                            .build()
                            .parseSignedClaims(token)
                            .getPayload();
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String generateAccessToken(String username, Integer userId) {
        return generateToken(username, userId);
    }

    public Integer getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                            .verifyWith(key)
                            .build()
                            .parseSignedClaims(token)
                            .getPayload();
            return Integer.valueOf(claims.get("userId", Integer.class));
        } catch (Exception e) {
            return null;
        }
    }

    private String generateToken(String username, Integer userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + ACCESS_TOKEN_VALIDITY);

        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)                
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

}
