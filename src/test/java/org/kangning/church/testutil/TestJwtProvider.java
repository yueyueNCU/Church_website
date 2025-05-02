package org.kangning.church.testutil;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.kangning.church.auth.domain.Role;
import org.kangning.church.common.identifier.UserId;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class TestJwtProvider {

    private static final String SECRET_KEY = "test-secret-key-for-test-only-1234567890";
    private static final Key SIGNING_KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public static String generateToken(UserId userId, String username, Set<Role> roles) {
        List<String> roleNames = roles.stream().map(Enum::name).toList();

        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("username", username)
                .claim("roles", roleNames)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1小時
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();
    }
}