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
    
    //메일 인증용 토큰
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
    
    //로그인용 Access Token
    public String createAccessToken(String email) {
    	return Jwts
    			.builder()
    			.setSubject("access-token")
    			.claim("email", email)
    			.setIssuedAt(new Date())
    			.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30 ))
    			.signWith(getSigningKey(), SignatureAlgorithm.HS256)
    			.compact();
    }
    
    //로그인용 Refresh Token
    public String createRefreshToken(String email) {
    	return Jwts
    			.builder()
    			.setSubject("refresh-token")
    			.claim("email", email)
    			.setIssuedAt(new Date())
    			.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30 ))
    			.signWith(getSigningKey(), SignatureAlgorithm.HS256)
    			.compact();
    }
    
    //토큰 검증
    public boolean verifyToken(String token) {
    	try {
    		parseToken(token);
    		return true;
    	} catch (Exception e) {
    		return false;
		}
    }
    
    //이메일 추출
    public String getEmail(String token) {
    	try {
    		return parseToken(token).get("email", String.class);
    	} catch (Exception e) {
			return null;
		}
    }
    
    private Claims parseToken(String token) {
    	return Jwts
    			.parserBuilder()
    			.setSigningKey(getSigningKey())
    			.build()
    			.parseClaimsJws(token)
    			.getBody();
    }
    
}

