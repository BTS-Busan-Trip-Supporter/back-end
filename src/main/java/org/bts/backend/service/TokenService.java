package org.bts.backend.service;

import org.bts.backend.dto.response.JwtTokenResponse;

public interface TokenService {

    // RefreshToken을 저장하는 메소드
    void updateRefreshToken(String Email, String uuid);

    // AccessToken, RefreshToken을 업데이트하는 메소드
    JwtTokenResponse updateAccessToken(String accessToken, String refreshToken);

    void logout(String accessToken);

}
