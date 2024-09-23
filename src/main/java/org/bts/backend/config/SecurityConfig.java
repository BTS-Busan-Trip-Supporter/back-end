package org.bts.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.bts.backend.auth_filter.AuthenticationFilter;
import org.bts.backend.auth_filter.AuthorizationFilter;
import org.bts.backend.exception.FilterExceptionHandler;
import org.bts.backend.service.TokenService;
import org.bts.backend.util.CookieProvider;
import org.bts.backend.util.JwtTokenProvider;
import org.bts.backend.util.OauthUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsFilter corsFilter;
    private final AuthenticationManager authenticationManager;
    private final CookieProvider cookieProvider;
    private final JwtTokenProvider jwtTokenProvider;
    private final OauthUtil oauthUtil;
    private final TokenService tokenService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 인증 필터 생성 및 의존성 주입
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager, cookieProvider, jwtTokenProvider,tokenService, new ObjectMapper());
        // 로그인 URL 설정
        authenticationFilter.setFilterProcessesUrl("/login");
        AuthorizationFilter AuthorizationFilter = new AuthorizationFilter(jwtTokenProvider);

        // 필터 체인 객체 생성
        http
                // 필터상의 예외를 필터로서 처리
                .addFilterBefore(new FilterExceptionHandler(new ObjectMapper()), CorsFilter.class)
                // csrf 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                // 우선 모든 요청에 대해 허용하도록 설정
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .anyRequest().permitAll())
                // 세션을 사용하지 않도록 설정
                .sessionManagement(sessionManagement ->
                                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 로그인 설정 비활성화 -> OAuth2 로그인 사용
                .formLogin(AbstractHttpConfigurer::disable)
                // 로그아웃 설정 비활성화
                .logout(AbstractHttpConfigurer::disable)
                // OAuth2 로그인 설정
                .oauth2Login(oauth ->
                        oauth.userInfoEndpoint(userInfo -> userInfo
                                .userService(oauthUtil))
                        .failureHandler(oauthUtil::oauthFailureHandler)
                        .successHandler(oauthUtil::oauthSuccessHandler))
                // CORS 필터 추가
                .addFilter(corsFilter)
                // 인증 필터 추가
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // 인가 필터 추가
                .addFilterAfter(AuthorizationFilter, AuthenticationFilter.class);
        return http.build();
    }
}
