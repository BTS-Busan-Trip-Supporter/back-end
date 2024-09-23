package org.bts.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bts.backend.dto.request.DayTripRequest;
import org.bts.backend.dto.request.ScheduleTripRequest;
import org.bts.backend.dto.request.ScheduleTripUpdateRequest;
import org.bts.backend.dto.request.TourActivityHistoryRequest;
import org.bts.backend.dto.request.TourActivityRecommendRequest;
import org.bts.backend.dto.response.ApiResponse;
import org.bts.backend.dto.response.DayTripResponse;
import org.bts.backend.dto.response.ScheduleTripResponse;
import org.bts.backend.dto.response.ScheduleTripsResponse;
import org.bts.backend.repository.TourActivityRepository;
import org.bts.backend.service.DayTripService;
import org.bts.backend.service.ScheduleTripService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TripController {

    private final TourActivityRepository tourActivityRepository;

    private final DayTripService dayTripService;
    private final ScheduleTripService scheduleTripService;

    @PostMapping("/trips/day")
    @Operation(summary = "당일치기 여행 관광지 추천", description = "DayTripRequest 를 기반으로 최대 10개의 관광지를 추천한다.")
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

    @PostMapping("/trips/schedule")
    @Operation(summary = "여행 일정 만들기", description = "ScheduleTripRequest 를 기반으로 여행 일정을 생성한다.")
    public void createScheduleTrip(@RequestBody ScheduleTripRequest scheduleTripRequest) {
        scheduleTripService.saveScheduleTrip("test@email.com", scheduleTripRequest.toTourLogDto(), scheduleTripRequest.tourActivityDtoList());
    }

    @GetMapping("/trips/schedule")
    @Operation(summary = "사용자의 모든 여행 일정 가져오기", description = "사용자의 모든 여행 일정을 불러온다. (페이지네이션 X)")
    public ResponseEntity<ApiResponse<ScheduleTripsResponse>> getAllScheduleTrips(
        @AuthenticationPrincipal Principal principal
    ) {
        log.info("user email: {}", principal.getName());
        return null;
    }

    @GetMapping("/trips/schedule/{logId}")
    @Operation(summary = "사용자의 여행 일정 단건 조회", description = "id가 logId인 여행 일정을 조회한다.")
    public ResponseEntity<ApiResponse<ScheduleTripResponse>> getScheduleTrip(
        @PathVariable Long logId
    ) {
        return ResponseEntity.ok(
            ApiResponse.success(
                scheduleTripService.getScheduleTrip(logId)
            )
        );
    }

    @PutMapping("/trips/schedule/{logId}")
    @Operation(summary = "여행 일정 수정", description = "ScheduleTripUpdateRequest 를 기반으로 여행 일정을 수정한다.")
    public void modifyScheduleTrip(
        @PathVariable Long logId,
        @RequestBody ScheduleTripUpdateRequest scheduleTripUpdateRequest
    ) {
        scheduleTripService.updateScheduleTrip(logId, scheduleTripUpdateRequest.toTourLogDto(), scheduleTripUpdateRequest.tourActivityDtoList());
    }

    @DeleteMapping("/trips/schedule/{logId}")
    @Operation(summary = "여행 일정 삭제", description = "id가 logId인 여행 일정을 삭제한다.")
    public void deleteScheduleTrip(
        @PathVariable Long logId
    ) {
        scheduleTripService.deleteScheduleTrip(logId);
    }

    @PutMapping("/trips/activity/recommend")
    @Operation(summary = "여행 일정의 관광지 추천/비추천", description = "TourActivityRecommendRequest 를 기반으로 추천/비추천한다.")
    public void recommendTourActivity(@RequestBody TourActivityRecommendRequest request) {
        scheduleTripService.recommendTourActivity(request.tourActivityId(), request.recommend());
    }

    @PutMapping("/trips/activity/history")
    @Operation(summary = "여행 일정의 관광지에 한줄평 작성/수정/삭제", description = "history 의 value가 null인 경우 삭제, 그외에는 작성 및 수정이 이루어진다.")
    public void modifyTourActivityHistory(@RequestBody TourActivityHistoryRequest request) {
        scheduleTripService.updateTourActivityHistory(request.tourActivityId(), request.history());
    }
}
