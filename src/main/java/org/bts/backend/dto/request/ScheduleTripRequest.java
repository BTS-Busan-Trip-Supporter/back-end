package org.bts.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import org.bts.backend.domain.constant.DayTime;
import org.bts.backend.dto.TourActivityDto;
import org.bts.backend.dto.TourLogDto;
import org.bts.backend.dto.TourSpotDto;

public record ScheduleTripRequest(
    @Schema(description = "여행 일정 데이터")
    TourLogData tourLogData,
    @Schema(description = "일정 별 관광지 데이터")
    List<TourActivityData> tourActivityDataList
) {

    public TourLogDto toTourLogDto() {
        return TourLogDto
            .builder()
            .name(tourLogData.name)
            .locationName(tourLogData.locationName)
            .startTime(tourLogData.startTime)
            .endTime(tourLogData.endTime)
            .build();
    }

    public List<TourActivityDto> tourActivityDtoList() {
        return tourActivityDataList
            .stream()
            .map(this::toTourActivityDto)
            .toList();
    }

    public TourActivityDto toTourActivityDto(TourActivityData tourActivityData) {
        return TourActivityDto
            .builder()
            .spotName(tourActivityData.spotName)
            .dayNumber(tourActivityData.dayNumber)
            .dayTime(tourActivityData.dayTime)
            .orderIndex(tourActivityData.orderIndex)
            .tourSpotDto(toTourSpotDto(tourActivityData.tourSpotData))
            .build();
    }

    public TourSpotDto toTourSpotDto(TourSpotData tourSpotData) {
        return TourSpotDto
            .builder()
            .id(tourSpotData.contentId)
            .typeId(tourSpotData.contentTypeId)
            .title(tourSpotData.title)
            .sigunguCode(tourSpotData.sigunguCode)
            .build();
    }

    public record TourLogData(
        @Schema(description = "여행 이름", example = "소소한 부산 여행")
        String name,
        @Schema(description = "여행 장소 이름", example = "친구들과 서면 여행")
        String locationName,
        @Schema(description = "여행 시작 날짜", example = "2024-09-21T00:00:00")
        LocalDateTime startTime,
        @Schema(description = "여행 끝나는 날짜", example = "2024-09-24T23:59:59")
        LocalDateTime endTime
    ) {

    }

    public record TourActivityData(
        @Schema(description = "정확한 관광지 이름", example = "KT&G 상상마당 부산")
        String spotName,
        @Schema(description = "일차")
        Integer dayNumber,
        @Schema(description = "시간대")
        DayTime dayTime,
        @Schema(description = "같은 시간대에 두개 이상의 관광지를 일정에 추가할 경우 순서를 보장하기 위함")
        Integer orderIndex,
        @Schema(description = "관광지 세부 데이터")
        TourSpotData tourSpotData
    ) {

    }

    public record TourSpotData(
        @Schema(description = "TourAPI에서 받아온 관광지의 id")
        String contentId,
        @Schema(description = "TourAPI에서 받아온 관광지의 contentId")
        String contentTypeId,
        @Schema(description = "관광지 이름")
        String title,
        @Schema(description = "TourAPI에서 받오온 관광지의 시/군/구 코드")
        String sigunguCode
    ) {

    }
}
