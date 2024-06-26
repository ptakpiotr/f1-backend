package zti.f1backend.util;

import io.jsonwebtoken.*;

import java.util.Date;

public class JwtUtils {
    private static final String SECRET = "your-secret-key-123-amazing-key-can-be-secure-213";
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days

    public static String generateToken(String username, int id) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", id)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public static String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}