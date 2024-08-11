package org.bts.backend.auth_filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bts.backend.dto.request.LoginRequest;
import org.bts.backend.util.CookieProvider;
import org.bts.backend.util.JwtTokenProvider;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final CookieProvider cookieProvider;
    private final JwtTokenProvider jwtTokenProvider;

    // todo: specify error handler
    // 인증 시도, HTTPRequest, HTTPResponse를 받아서 인증을 시도하는 메소드
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication;
        try {
            // request의 body에서 email, password를 읽어옴
            LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);

            // AuthenticationManager를 통해 Authentication 객체를 생성
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password())
            );
        } catch (IOException e) {
            throw new IllegalArgumentException("로그인 정보를 읽어오는데 실패했습니다.");
        }
        return authentication;
    }

    // 인증(attemptAuthentication) 성공 시 실행되는 메소드
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // Authentication 객체에서 email을 추출
        String email = authResult.getName();

        // Authentication 객체에서 권한을 추출
        List<String> roles = authResult.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        // email을 이용해 AccessToken, RefreshToken을 생성
        String accessToken = jwtTokenProvider.createAccessToken(email, roles);
        String refreshToken = jwtTokenProvider.createRefreshToken();

        // RefreshToken을 쿠키에 저장
        ResponseCookie refreshCookie = cookieProvider.createRefreshCookie(refreshToken);
        response.addCookie(cookieProvider.of(refreshCookie));

        // AccessToken을 Response Header에 저장
        response.addHeader("Authorization", accessToken);
    }
}
