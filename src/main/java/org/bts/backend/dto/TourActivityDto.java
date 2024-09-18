package org.bts.backend.dto;

import lombok.Builder;
import org.bts.backend.domain.TourActivity;
import org.bts.backend.domain.TourLog;
import org.bts.backend.domain.TourSpot;
import org.bts.backend.domain.constant.DayTime;

@Builder
public record TourActivityDto(
    long id,
    String spotName,
    Boolean recommend,
    Integer dayNumber,
    DayTime dayTime,
    Integer orderIndex,
    TourLogDto tourLogDto,
    TourSpotDto tourSpotDto
) {

    public static TourActivityDto of(TourActivity entity) {
        return new TourActivityDto(
            entity.getId(),
            entity.getSpotName(),
            entity.getRecommend(),
            entity.getDayNumber(),
            entity.getDayTime(),
            entity.getOrderIndex(),
            TourLogDto.of(entity.getTourLog()),
            TourSpotDto.of(entity.getTourSpot())
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