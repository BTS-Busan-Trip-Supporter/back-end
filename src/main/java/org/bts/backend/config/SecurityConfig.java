package org.bts.backend.config;

import lombok.RequiredArgsConstructor;
import org.bts.backend.auth_filter.AuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationFilter authenticationFilter;

    // TODO: 패스워드 인코딩 전략 정해서 빈으로 등록시킬 것
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 필터 객체 생성
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
                        .deleteCookies("REFRESHTOKEN"))
                // 인증 필터 추가
                .addFilter(authenticationFilter);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return new ProviderManager(provider);
    }

}
