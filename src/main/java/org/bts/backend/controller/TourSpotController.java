package org.bts.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bts.backend.dto.response.ApiResponse;
import org.bts.backend.dto.response.TourSpotDetailResponse;
import org.bts.backend.dto.response.TourSpotResponse;
import org.bts.backend.service.TourAPIService;
import org.bts.backend.service.TourSpotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TourSpotController {

    private final TourAPIService tourAPIService;
    private final TourSpotService tourSpotService;

    @GetMapping("/tourspots/{contentId}")
    @Operation(summary = "관광지 정보 확인", description = "figma #4-3 구체적인 관광지 정보를 확인할 수 있다.")
    public ResponseEntity<ApiResponse<TourSpotDetailResponse>> getTourSpotDetail(
        @PathVariable String contentId,
        @RequestParam String contentTypeId
    ) {
        return ResponseEntity.ok(ApiResponse.success(tourSpotService.getTourSpotDetailResponse(contentId, contentTypeId)));
    }

    @GetMapping("/tourspots")
    @Operation(summary = "관광지 검색", description = "figma #5-3-1 관광지명과 시/군/구 코드를 기반으로 해당하는 관광지 1개를 검색 결과로 반환합니다.")
    public ResponseEntity<ApiResponse<TourSpotResponse>> getTourSpot(
        @RequestParam String keyword,
        @RequestParam String sigunguCode
    ) {
        return ResponseEntity.ok(ApiResponse.success(tourSpotService.getTourSpotResponse(keyword, sigunguCode)));
    }

    // TODO: 인가 관련해서 관리자만 실행시킬 수 있도록 할 것
    @PostMapping("/admin/tourdata")
    @Operation(summary = "관리자용", description = "서비스가 처음 실행될 때 부산광역시에 대한 관광지 정보를 데이터베이스에 저장한다.")
    public ResponseEntity<ApiResponse<String>> initTourData() {
        tourAPIService.saveAllTourData();
        return ResponseEntity.ok(ApiResponse.success("데이터베이스에 관광 공사 데이터 적재 성공"));
    }
}
