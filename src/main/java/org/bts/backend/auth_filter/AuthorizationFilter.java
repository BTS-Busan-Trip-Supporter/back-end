package org.bts.backend.auth_filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bts.backend.util.JwtTokenProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter{
    private final JwtTokenProvider jwtTokenProvider;
    // 필터링을 통해 요청이 들어왔을 때, 해당 요청이 허용되는지 확인하는 메소드
    // 토큰 검사후, @Secured를 위해 Authentication 객체 발행.
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("Enter AuthorizationFilter");
        // 헤더에서 토큰 추출
        String token = request.getHeader("Authorization");
        // 토큰이 없거나 이상하면 -> 익명권한 부여
        if(token == null || !token.startsWith("Bearer ")){
            // 권한 부여, Role -> {"ROLE_ANONYMOUS"}
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add((GrantedAuthority) () -> "ROLE_ANONYMOUS");

            // 이메일 -> ROLE_ANONYMOUS, 비밀번호 -> null 로 Authentication 객체 생성
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    "ROLE_ANONYMOUS", null, authorities);

            // SecurityContextHolder에 Authentication 객체 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
            return;
        }
        token = token.substring(7);
        // 정상 토큰 검사
        if(jwtTokenProvider.validateToken(token)) {
            // 토큰에서 정보 추출.
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            for (String role : jwtTokenProvider.getRole(token)) {
                authorities.add((GrantedAuthority) () -> role);
            }

            // Authentication 객체 생성
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    jwtTokenProvider.getEmailForAccessToken(token), null, authorities);

            // SecurityContextHolder에 Authentication 객체 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }
    }
}
