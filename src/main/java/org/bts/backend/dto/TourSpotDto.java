package org.bts.backend.dto;

import lombok.Builder;
import org.bts.backend.domain.TourSpot;

@Builder
public record TourSpotDto(
    String id,
    String typeId,
    String title,
    String sigunguCode
){

    public static TourSpotDto of(TourSpot entity) {
        return new TourSpotDto(
            entity.getId(),
            entity.getTypeId(),
            entity.getTitle(),
            entity.getSigunguCode()
        );
    }
}