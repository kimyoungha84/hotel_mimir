package kr.co.sist.config;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.sist.member.CustomUserDetails; // CustomUserDetails 임포트
import kr.co.sist.util.LoginJwtUtil;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final LoginJwtUtil loginJwtUtil;

    public JwtAuthFilter(LoginJwtUtil loginJwtUtil) {
        this.loginJwtUtil = loginJwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null && loginJwtUtil.validateToken(token)) {
            Claims claims = loginJwtUtil.getClaims(token);
            String email = claims.get("email_id", String.class);
            Integer userNum = claims.get("user_num", Integer.class); // user_num 클레임 가져오기

            // CustomUserDetails 객체 생성
            CustomUserDetails userDetails = new CustomUserDetails(email, "", new ArrayList<>(), userNum);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
