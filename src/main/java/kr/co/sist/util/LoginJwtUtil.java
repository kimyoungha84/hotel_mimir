package kr.co.sist.util;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class LoginJwtUtil {
	
	//로그인용
	@Value("${jwt.secret}")
	private String secretKey;
	
	private final long accessTokenValidity = 1000 * 60 * 15; //15분
	private final long refreshTokenvalidity = 1000L * 60 * 60 * 24 * 14; //14일
	
	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	public String generateAccessToken(int userNum, String emailId, String name) {
		
		return Jwts.builder()
				.setSubject("AccessToken") //토큰 주제
				.claim("user_num", userNum)
				.claim("email_id", emailId)
				.claim("name", name)
				.setIssuedAt(new Date()) //토큰 발급 시각
				.setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))//토큰 만료시간
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)//서명 알고리즘
				.compact(); //생성
	}
	
	public String generateRefreshToken(int userNum) {
		return Jwts.builder()
				.setSubject("RefreshToken") //토큰 주제
				.claim("user_num", userNum)
				.setIssuedAt(new Date()) //토큰 발급 시각
				.setExpiration(new Date(System.currentTimeMillis() + refreshTokenvalidity))//토큰 만료시간
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)//서명 알고리즘
				.compact(); //생성
	}
	
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public Claims getClaims(String token) {
		return Jwts.parserBuilder()
					.setSigningKey(getSigningKey())
					.build()
					.parseClaimsJws(token)
					.getBody();
	}

    //메일 인증용
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
