package org.bts.backend.dto.response;

import org.bts.backend.domain.Member;

public record MemberResponse(
    String email,
    String name
) {

    public static MemberResponse toResponse(Member entity) {
        return new MemberResponse(
            entity.getEmail(),
            entity.getName()
        );
    }
}
