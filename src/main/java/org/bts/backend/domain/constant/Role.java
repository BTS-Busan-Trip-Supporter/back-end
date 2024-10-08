package org.bts.backend.domain.constant;

import lombok.Getter;

import java.util.List;

@Getter
public enum Role {

    // 이메일 인증 전에 부여되는 권한, 소셜로그인에서는 해당 없음
    ROLE_UNVERIFIED("ROLE_UNVERIFIED"),
    // 이메일 인증 완료 혹은 소셜로그인시에 부여되는 권한
    ROLE_VERIFIED("ROLE_VERIFIED"),

    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN"),

    // 익명
    ROLE_ANONYMOUS("ROLE_ANONYMOUS");

    final String role;

    Role(String role) {
        this.role = role;
    }
}
