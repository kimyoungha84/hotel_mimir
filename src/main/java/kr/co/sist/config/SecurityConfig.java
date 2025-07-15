package kr.co.sist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfig {
	
	private final JwtAuthFilter jwtAuthFilter;
	
	public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
		this.jwtAuthFilter = jwtAuthFilter;
	}

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (필요 시 유지 가능)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/**").permitAll() // 모든 경로 허용
                .anyRequest().permitAll()
            )
            .formLogin(form -> form.disable()) // 기본 로그인 폼 비활성화
            .logout(logout -> logout
                .logoutUrl("/member/logout") // 로그아웃을 처리할 URL
                .logoutSuccessUrl("/")       // 로그아웃 성공 후 리다이렉트할 URL
                .invalidateHttpSession(false) // JWT는 세션 기반이 아니므로 세션 무효화는 false
                .deleteCookies("access_token") // 로그아웃 시 access_token 쿠키 삭제
                .permitAll() // 로그아웃 URL은 누구나 접근 가능하도록 허용
            )
        	.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
