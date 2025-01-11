package rs.raf.reservation_service.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secretKeyString;

    private Key secretKey;

    private static final long EXPIRATION_TIME = 86400000L; // 1 day in milliseconds
    private static final String ISSUER = "software-component";    // Customize the issuer

    @PostConstruct
    public void init() {
        // Initialize the Key after the secret is injected
        this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }

    /**
     * Generates a JWT token for the given subject.
     *
     * @param username The subject for whom the token is being created (e.g., username or user ID).
     * @return The JWT token as a string.
     */
    public String generateToken(Long id, String username) {
        // Build the JWT token
        String subject = id + ":" + username + "-" + "SERVER";
        return Jwts.builder()
                .setSubject(subject)                   // The subject (e.g., user)
                .setIssuer(ISSUER)                               // Issuer
                .setIssuedAt(new Date())                        // Token creation time
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Expiry
                .signWith(secretKey, SignatureAlgorithm.HS256)  // Sign with the secret key
                .compact();
    }

    /**
     * Validates the JWT token and parses its claims.
     *
     * @param token The JWT token to validate.
     * @return The claims inside the token if it is valid.
     * @throws JwtException If the token is invalid, expired, or tampered with.
     */
    public Claims validateToken(String token) {
        try {
            // Parse and validate the JWT token
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey) // Use the same key for verification
                    .requireIssuer(ISSUER)    // Ensure the issuer matches
                    .build()
                    .parseClaimsJws(token)    // Parse and verify
                    .getBody();               // Get the token claims
        } catch (JwtException e) {
            throw new RuntimeException("Invalid or expired token: " + e.getMessage());
        }
    }
}
