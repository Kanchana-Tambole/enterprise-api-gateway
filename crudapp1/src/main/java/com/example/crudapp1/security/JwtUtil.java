package com.example.crudapp1.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * Utility class for generating and validating JWT tokens.
 */
@Component
public class JwtUtil {

    // =======================
    // Constants and Secrets
    // =======================

    // Strong 256-bit key (should be 32+ characters) — move to application.properties or env variable in real apps
    private static final String SECRET = "replace_this_with_a_very_long_and_secure_secret_key_1234567890";

    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    private static final long JWT_EXPIRATION_MILLIS = 1000 * 60 * 60 * 10; // 10 hours

    // =======================
    // Public API
    // =======================

    /**
     * Generates a JWT token for the given username.
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MILLIS))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the username (subject) from a token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Validates a token by checking username match and expiry.
     */
    public boolean validateToken(String token, String expectedUsername) {
        try {
            String tokenUsername = extractUsername(token);
            return tokenUsername.equals(expectedUsername) && !isTokenExpired(token);
        } catch (Exception e) {
            return false; // Malformed or expired token
        }
    }

    // =======================
    // Internal Helpers
    // =======================

    /**
     * Extracts a claim from the token using a resolver function.
     */
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    /**
     * Checks if a token is expired.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the token expiration date.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Parses and returns all claims from the token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}


