package org.bts.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final UserDetailsService userDetailsService;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter() {
        // 경로별 Cors 설정
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Cors 설정
        // todo: specify allowed origins, methods, headers
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");

        // 모든경로에 대해 위의 설정을 적용
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

    // AuthenticationManager 빈 등록 -> DaoAuthenticationProvider 를 통해 인증로직 설정 및 암호 인코딩, 사용자 세부 정보 검색 관리.
    @Bean
    public AuthenticationManager authenticationManager(){
        // 데이터베이스에 저장된 사용자 정보를 기반으로 인증
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // 비밀번호 인코딩 및 검증
        provider.setPasswordEncoder(passwordEncoder());
        // 사용자 세부 정보 검색
        provider.setUserDetailsService(userDetailsService);
        return new ProviderManager(provider);
    }
}
