package org.kangning.church.auth.adapter.out.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.kangning.church.auth.application.port.out.JwtProviderPort;
import org.kangning.church.auth.domain.Role;
import org.kangning.church.common.exception.auth.jwt.InvalidTokenException;
import org.kangning.church.common.identifier.UserId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class JwtProviderAdapter implements JwtProviderPort {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expiration-ms}")
    private long expirationMs;


    private Key signingKey;

    @PostConstruct
    public void init() {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    public String generateToken(UserId userId, String username, Set<Role> roles) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("username", username)
                .claim("roles",roles.stream().map(Enum::name).toList())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }


    @Override
    public UserId extractUserId(String token) {
        return new UserId(Long.parseLong(getClaims(token).getSubject()));
    }

    @Override
    public String extractUsername(String token) {
        return getClaims(token).get("username", String.class);
    }

    @Override
    public Set<Role> extractRoles(String token) {
        List<String> roleNames = getClaims(token).get("roles", List.class); // ✅ List
        return roleNames.stream()
                .map(Role::valueOf)
                .collect(java.util.stream.Collectors.toSet()); // ✅ 轉成 Set<Role>
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException();
        }
    }
}
