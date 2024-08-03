package org.bts.backend.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bts.backend.domain.constant.AuthProvider;
import org.bts.backend.domain.constant.Role;
import org.bts.backend.domain.converter.AuthProviderConverter;

@Entity
@Table(
    indexes = {
    @Index(columnList = "email", unique = true),
    @Index(columnList = "password")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 30)
    private String name;

    private String password;

    @Convert(converter = AuthProviderConverter.class)
    @Column(nullable = false)
    private AuthProvider authProvider;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private final List<Role> roles = new ArrayList<>();

    // -- 생성자 메서드 -- //
    private Member(String email, String name, String password, AuthProvider authProvider) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.authProvider = authProvider;
    }

    public static Member of(
        String email,
        String name,
        String password,
        AuthProvider authProvider
    ) {
        return new Member(email, name, password, authProvider);
    }

    // -- 비지니스 로직 (검증, setter) -- //

    // -- Equals & Hash -- //
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Member member)) {
            return false;
        }
        return Objects.equals(id, member.id) && Objects.equals(email, member.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
