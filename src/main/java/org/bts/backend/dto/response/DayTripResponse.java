package org.bts.backend.dto.response;

public record DayTripResponse(
    String contentId,
    String contentTypeId,
    String title,
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
