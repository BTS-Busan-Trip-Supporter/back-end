package org.bts.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import org.bts.backend.domain.TourActivity;
import org.bts.backend.domain.TourLog;
import org.bts.backend.domain.TourSpot;
import org.bts.backend.domain.constant.DayTime;

@Builder
public record TourActivityDto(
    Long id,
    String spotName,
    Boolean recommend,
    String history,
    Integer dayNumber,
    DayTime dayTime,
    Integer orderIndex,
    @JsonIgnore
    TourLogDto tourLogDto,
    TourSpotDto tourSpotDto,
    @JsonIgnore
    Boolean isOrderChanged,
    @JsonIgnore
    Boolean isDeleted
) {

    public static TourActivityDto of(TourActivity entity) {
        return new TourActivityDto(
            entity.getId(),
            entity.getSpotName(),
            entity.getRecommend(),
            entity.getHistory(),
            entity.getDayNumber(),
            entity.getDayTime(),
            entity.getOrderIndex(),
            TourLogDto.of(entity.getTourLog()),
            TourSpotDto.of(entity.getTourSpot()),
            false,
            false
        );
    }

    public TourActivity toEntity(TourLog tourLog, TourSpot tourSpot) {
        return TourActivity.of(
            spotName,
            dayNumber,
            tourSpot,
            tourLog,
            dayTime,
            orderIndex
        );
    }
}