package org.bts.backend.dto.request;

public record TourActivityHistoryRequest(
    Long tourActivityId,
    String history
) {

}
