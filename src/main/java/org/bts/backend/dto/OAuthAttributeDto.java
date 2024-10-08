package org.bts.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bts.backend.domain.Member;
import org.bts.backend.domain.constant.AuthProvider;
import org.bts.backend.domain.constant.Role;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
public class OAuthAttributeDto {
    private Map<String, Object> attributes;
    private String name;
    private String email;
    private String nameAttributeKey;
    private AuthProvider provider;

    // 내부 생성자
    public OAuthAttributeDto(Map<String, Object> attributes, String name, String email, String nameAttributeKey, AuthProvider provider) {
        this.attributes = attributes;
        this.name = name;
        this.email = email;
        this.nameAttributeKey = nameAttributeKey;
        this.provider = provider;
    }

    // 팩토리 메서드, 외부공개용.
    public static OAuthAttributeDto of(Map<String, Object> attributes, String nameAttributeKey, AuthProvider provider) {
        if (AuthProvider.KAKAO.equals(provider)) {
            return ofKakao(attributes, nameAttributeKey);
        }
        return null;
    }

    // 팩토리 메서드 구현체.
    private static OAuthAttributeDto ofKakao(Map<String, Object> attributes, String nameAttributeKey) {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        return OAuthAttributeDto.builder()
                .attributes(attributes)
                .name(properties.get("nickname").toString())
                .email(kakaoAccount.get("email").toString())
                .nameAttributeKey(nameAttributeKey)
                .provider(AuthProvider.KAKAO)
                .build();
    }

    public Member toEntity() {
        List<Role> roles = List.of(Role.ROLE_USER, Role.ROLE_VERIFIED);
        // id + provider로 email을 만들어줌.
        String email = this.attributes.get(this.nameAttributeKey) + "@" + this.provider.toString().toLowerCase();
        return Member.of(email, name, "", provider, roles);
    }
}
