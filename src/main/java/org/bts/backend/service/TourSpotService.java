package org.bts.backend.service;

import org.bts.backend.dto.response.TourSpotDetailResponse;

public interface TourSpotService {
    TourSpotDetailResponse getTourSpotDetailResponse(String contentId, String contentTypeId);
}
