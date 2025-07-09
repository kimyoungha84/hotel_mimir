package kr.co.sist.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateEmailAuthToken(String email, String authCode, long expirationMillis) {
        return Jwts.builder()
                .setSubject("email-auth")
                .claim("email", email)
                .claim("authCode", authCode)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateEmailAuth(String token, String email, String code) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String tokenEmail = claims.get("email", String.class);
            String tokenCode = claims.get("authCode", String.class);

            return email.equals(tokenEmail) && code.equals(tokenCode);
        } catch (Exception e) {
            return false;
        }
    }
}

