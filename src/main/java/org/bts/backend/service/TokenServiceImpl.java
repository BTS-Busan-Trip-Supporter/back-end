package org.bts.backend.service;

import lombok.RequiredArgsConstructor;
import org.bts.backend.domain.Member;
import org.bts.backend.dto.response.JwtTokenResponse;
import org.bts.backend.exception.before_servlet.CustomTokenException;
import org.bts.backend.exception.after_servlet.UsernameNotFoundException;
import org.bts.backend.repository.MemberRepository;
import org.bts.backend.repository.RefreshTokenRedisRepository;
import org.bts.backend.util.JwtTokenProvider;
import org.bts.backend.domain.RefreshToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService{
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRedisRepository refreshTokenRepository;

    // todo: specify error handler
    @Override
    public void updateRefreshToken(String email, String uuid) {
        // 사용자 체크
        if(memberRepository.existsByEmail(email))
            refreshTokenRepository.save(RefreshToken.of(email, uuid));
        else {
            throw new UsernameNotFoundException("해당 이메일을 가진 사용자가 존재하지 않습니다.");
        }
    }

    // todo: specify error handler
    @Override
    public JwtTokenResponse updateAccessToken(String accessToken, String refreshToken) {
        // 사용자 체크
        Member member = memberRepository.findByEmail(jwtTokenProvider.getEmailForAccessToken(accessToken)).orElseThrow(
                () -> new UsernameNotFoundException("해당 이메일을 가진 사용자가 존재하지 않습니다.")
        );
        // 토큰 존재여부 체크
        RefreshToken refreshTokenEntity = refreshTokenRepository.findById(member.getEmail()).orElseThrow(
                () -> new CustomTokenException("해당 이메일을 가진 사용자의 RefreshToken이 존재하지 않습니다.")
        );
        // 토큰 유효여부 체크
        jwtTokenProvider.validateToken(refreshToken);

        // 토큰 동일여부 체크
        if(!jwtTokenProvider.sameRefreshToken(refreshToken, refreshTokenEntity.getRefreshTokenId())) {
            throw new CustomTokenException("RefreshToken이 일치하지 않습니다.");
        }
        // 토큰 갱신
        List<String> roles = jwtTokenProvider.getRole(accessToken);
        String newAccessToken = jwtTokenProvider.createAccessToken(member.getEmail(), roles);
        return JwtTokenResponse.toResponse(newAccessToken, refreshToken);
    }

    @Override
    public void logout(String accessToken) {
        // 토큰 유효여부 체크
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new CustomTokenException("토큰이 유효하지 않습니다.");
        }
        // 사용자 체크 및 refreshToken 존재여부 체크
        RefreshToken refreshTokenEntity = refreshTokenRepository.findById(jwtTokenProvider.getEmailForAccessToken(accessToken)).orElseThrow(
                () -> new CustomTokenException("해당 이메일을 가진 사용자의 RefreshToken이 존재하지 않습니다.")
        );
        // 토큰 삭제
        refreshTokenRepository.delete(refreshTokenEntity);
    }
}
