package org.bts.backend.dto.request;

public record MemberRequest(
    String email,
    String password,
    String name
) {

}
