package org.bts.backend.service;

import java.util.Map;
import org.bts.backend.dto.response.tourapi.DetailCommonResponse;
import org.bts.backend.dto.response.tourapi.LocationBasedResponse;
import org.bts.backend.dto.response.tourapi.SearchKeywordResponse;
import reactor.core.publisher.Mono;

public interface TourAPIService {
    Mono<LocationBasedResponse> getLocationBasedResponse(String mapX, String mapY, String radius, Map<String, String> additionalParams);
    Mono<SearchKeywordResponse> getSearchKeywordResponse(String keyword, Map<String, String> additionalParams);
    Mono<DetailCommonResponse> getDetailCommonResponse(String contentId, Map<String, String> additionalParams);
}
