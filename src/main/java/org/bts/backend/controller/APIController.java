package org.bts.backend.controller;

import lombok.RequiredArgsConstructor;
import org.bts.backend.dto.response.tourapi.LocationBasedResponse;
import org.bts.backend.service.TourAPIService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class APIController {

    private final TourAPIService tourAPIService;

    @GetMapping("/test/api1")
    public Mono<LocationBasedResponse> getLocationBased() {
        return tourAPIService.getLocationBasedResponse();
    }

}
