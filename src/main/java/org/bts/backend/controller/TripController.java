package org.bts.backend.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bts.backend.dto.request.DayTripRequest;
import org.bts.backend.dto.response.ApiResponse;
import org.bts.backend.dto.response.DayTripResponse;
import org.bts.backend.service.DayTripService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TripController {

    private final DayTripService dayTripService;

    @PostMapping("/trips/day")
    public ResponseEntity<ApiResponse<List<DayTripResponse>>> getRecommendDayTrip(
        @RequestBody DayTripRequest dayTripRequest
    ) {
        return ResponseEntity.ok(
            ApiResponse.success(
                dayTripService.recommendDayTourSpots(
                    dayTripRequest.contentTypeId(),
                    dayTripRequest.sigunguCode(),
                    dayTripRequest.dayTimes()
                )
            )
        );
    }
}
