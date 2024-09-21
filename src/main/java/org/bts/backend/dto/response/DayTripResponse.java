package org.bts.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record DayTripResponse(
    String contentId,
    String contentTypeId,
    String title,
    @Schema(description = "이미지 url / 빈 공백 문자인 경우 해당하는 이미지 url 없는 것")
    String imageUrl
) {

    public static DayTripResponse of(
        String contentId,
        String contentTypeId,
        String title,
        String imageUrl
    ) {
        return new DayTripResponse(contentId, contentTypeId, title, imageUrl);
    }

}
