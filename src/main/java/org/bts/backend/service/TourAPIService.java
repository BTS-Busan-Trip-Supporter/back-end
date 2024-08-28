package org.bts.backend.service;

import org.bts.backend.dto.response.tourapi.LocationBasedResponse;
import reactor.core.publisher.Mono;

public interface TourAPIService {
    Mono<LocationBasedResponse> getLocationBasedResponse();
}
