package org.bts.backend.dto.response.tourapi;

public record Response<T>(
    Header header,
    T body
) {}