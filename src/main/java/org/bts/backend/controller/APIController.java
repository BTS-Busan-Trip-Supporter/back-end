package org.bts.backend.controller;

import lombok.RequiredArgsConstructor;
import org.bts.backend.dto.response.ApiResponse;
import org.bts.backend.dto.response.tourapi.DetailCommonResponse;
import org.bts.backend.dto.response.tourapi.DetailIntroResponse;
import org.bts.backend.dto.response.tourapi.LocationBasedResponse;
import org.bts.backend.dto.response.tourapi.SearchKeywordResponse;
import org.bts.backend.service.TourAPIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/test/api4")
    public ResponseEntity<SearchKeywordResponse> getSearchKeywordBlock() {
        return ResponseEntity.ok(tourAPIService.getSearchKeywordResponse("부산", null).block());
    }

    @GetMapping("/test/api5")
    public Mono<DetailIntroResponse> getDetailIntroResponse() {
        return tourAPIService.getDetailIntroResponse("2834026", "14", null);
    }

    // TODO: 인가 관련해서 관리자만 실행시킬 수 있도록 할 것
    @PostMapping("/admin/tourdata")
    public ResponseEntity<ApiResponse<String>> initTourData() {
        tourAPIService.saveAllTourData("부산");
        return ResponseEntity.ok(ApiResponse.success("데이터베이스에 관광 공사 데이터 적재 성공"));
    }
}
