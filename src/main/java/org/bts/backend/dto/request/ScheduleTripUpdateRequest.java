package org.bts.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import org.bts.backend.domain.constant.DayTime;
import org.bts.backend.dto.TourActivityDto;
import org.bts.backend.dto.TourLogDto;
import org.bts.backend.dto.TourSpotDto;


public record ScheduleTripUpdateRequest(
    TourLogData tourLogData,
    List<TourActivityData> tourActivityDataList
) {

    public TourLogDto toTourLogDto() {
        return TourLogDto
            .builder()
            .id(tourLogData.id)
            .name(tourLogData.name)
            .locationName(tourLogData.locationName)
            .startTime(tourLogData.startTime)
            .endTime(tourLogData.endTime)
            .build();
    }

    public List<TourActivityDto> tourActivityDtoList() {
        return tourActivityDataList
            .stream()
            .filter(tourActivityData -> tourActivityData.isNew
                || tourActivityData.isOrderChanged
                || tourActivityData.isTourSpotChanged
                || tourActivityData.isDeleted
            )
            .map(this::toTourActivityDto)
            .toList();
    }

    public TourActivityDto toTourActivityDto(TourActivityData tourActivityData) {
        return TourActivityDto
            .builder()
            .id(tourActivityData.id)
            .spotName(tourActivityData.spotName)
            .dayNumber(tourActivityData.dayNumber)
            .dayTime(tourActivityData.dayTime)
            .orderIndex(tourActivityData.orderIndex)
            .tourSpotDto(toTourSpotDto(tourActivityData.tourSpotData))
            .isOrderChanged(tourActivityData.isOrderChanged)
            .isDeleted(tourActivityData.isDeleted)
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
        Long id,
        String name,
        String locationName,
        LocalDateTime startTime,
        LocalDateTime endTime
    ) {

    }

    public record TourActivityData(
        Long id,
        String spotName,
        Integer dayNumber,
        DayTime dayTime,
        Integer orderIndex,
        TourSpotData tourSpotData,
        @Schema(description = "일정 수정 시 새롭게 추가된 것인지")
        Boolean isNew,
        @Schema(description = "일정 수정 시 시간대가 변경된 것인지")
        Boolean isOrderChanged,
        @Schema(description = "일정 수정 시 예정 했던 관광지가 변경된 것인지")
        Boolean isTourSpotChanged,
        @Schema(description = "일정 수정 시 기존에 추가 했었지만 삭제된 것인지")
        Boolean isDeleted
    ) {

    }

    public record TourSpotData(
        String contentId,
        String contentTypeId,
        String title,
        String sigunguCode
    ) {

    }
}
