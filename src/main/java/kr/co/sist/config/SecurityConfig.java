package kr.co.sist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (필요 시 유지 가능)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/**").permitAll() // 모든 경로 허용
                .anyRequest().permitAll()
            )
            .formLogin(form -> form.disable()); // 기본 로그인 폼 비활성화

        return http.build();
    }
}
