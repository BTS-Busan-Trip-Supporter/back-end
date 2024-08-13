package org.bts.backend.dto.request;

public record MailCertRequest(
    String email,
    String uuid
) {
}
