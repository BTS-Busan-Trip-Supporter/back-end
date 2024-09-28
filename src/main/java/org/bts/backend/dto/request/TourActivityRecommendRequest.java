package org.bts.backend.dto.request;

public record TourActivityRecommendRequest(
    Long tourActivityId,
    Boolean recommend
) {

}
