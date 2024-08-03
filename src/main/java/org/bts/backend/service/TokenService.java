package org.bts.backend.service;

public interface TokenService {

    // RefreshToken을 저장하는 메소드
    void updateRefreshToken(String Email, String uuid);

    // AccessToken, RefreshToken을 업데이트하는 메소드
    void updateAccessToken(String accessToken, String refreshToken);

    void logout(String Email);

}
