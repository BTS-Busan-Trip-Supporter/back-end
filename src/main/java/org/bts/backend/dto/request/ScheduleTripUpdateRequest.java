package org.bts.backend.dto.request;

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
        Boolean isNew,
        Boolean isOrderChanged,
        Boolean isTourSpotChanged,
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
