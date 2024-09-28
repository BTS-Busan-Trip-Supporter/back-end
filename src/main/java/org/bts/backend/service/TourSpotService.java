package org.bts.backend.service;

import org.bts.backend.dto.response.TourSpotDetailResponse;
import org.bts.backend.dto.response.TourSpotResponse;

public interface TourSpotService {
    TourSpotDetailResponse getTourSpotDetailResponse(String contentId, String contentTypeId);

    TourSpotResponse getTourSpotResponse(String keyword, String sigunguCode);
}
