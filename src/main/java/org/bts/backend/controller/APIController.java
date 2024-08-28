package org.bts.backend.controller;

import lombok.RequiredArgsConstructor;
import org.bts.backend.dto.response.tourapi.DetailCommonResponse;
import org.bts.backend.dto.response.tourapi.LocationBasedResponse;
import org.bts.backend.dto.response.tourapi.SearchKeywordResponse;
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
        return tourAPIService.getLocationBasedResponse("128.878492", "37.74913611", "1000", null);
    }

    @GetMapping("/test/api2")
    public Mono<SearchKeywordResponse> getSearchKeyword() {
        return tourAPIService.getSearchKeywordResponse("서면", null);
    }

    @GetMapping("/test/api3")
    public Mono<DetailCommonResponse> getDetailCommonResponse() {
        return tourAPIService.getDetailCommonResponse("2465063", null);
    }
}
