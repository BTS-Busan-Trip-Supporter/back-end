package org.bts.backend.config;

import lombok.RequiredArgsConstructor;
import org.bts.backend.auth_filter.AuthenticationFilter;
import org.bts.backend.util.CookieProvider;
import org.bts.backend.util.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsFilter corsFilter;
    private final AuthenticationManager authenticationManager;
    private final CookieProvider cookieProvider;
    private final JwtTokenProvider jwtTokenProvider;

    // TODO: 패스워드 인코딩 전략 정해서 빈으로 등록시킬 것
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 인증 필터 생성 및 의존성 주입
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager, cookieProvider, jwtTokenProvider);
        // 로그인 URL 설정
        authenticationFilter.setFilterProcessesUrl("/login");
        // 필터 체인 객체 생성
        http
                // csrf 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                // 우선 모든 요청에 대해 허용하도록 설정
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .anyRequest().permitAll())
                // 세션을 사용하지 않도록 설정
                .sessionManagement(sessionManagement ->
                                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 로그아웃 설정(쿠키 삭제)
                .logout((logout) -> logout.logoutUrl("/logout")
                        .deleteCookies("RefreshToken"))
                // 인증 필터 추가
                .addFilter(authenticationFilter)
                // CORS 필터 추가
                .addFilter(corsFilter);
        return http.build();
    }
}
