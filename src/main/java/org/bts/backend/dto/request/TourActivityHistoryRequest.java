package org.bts.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record TourActivityHistoryRequest(
    Long tourActivityId,
    @Schema(description = "한줄평")
    String history
) {

}
