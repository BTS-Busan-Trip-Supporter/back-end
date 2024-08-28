package org.bts.backend.service;

import java.util.Map;
import org.bts.backend.dto.response.tourapi.LocationBasedResponse;
import reactor.core.publisher.Mono;

public interface TourAPIService {
    Mono<LocationBasedResponse> getLocationBasedResponse(String mapX, String mapY, String radius, Map<String, String> additionalParams);
}
