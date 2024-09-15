package org.bts.backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bts.backend.dto.response.ApiResponse;
import org.bts.backend.service.TourAPIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class APIController {

    private final TourAPIService tourAPIService;

    // TODO: 인가 관련해서 관리자만 실행시킬 수 있도록 할 것
    @PostMapping("/admin/tourdata")
    public ResponseEntity<ApiResponse<String>> initTourData() {
        tourAPIService.saveAllTourData();
        return ResponseEntity.ok(ApiResponse.success("데이터베이스에 관광 공사 데이터 적재 성공"));
    }
}
