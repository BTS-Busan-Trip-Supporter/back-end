package org.bts.backend.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import org.bts.backend.domain.Member;
import org.bts.backend.domain.TourLog;

@Builder
public record TourLogDto(
    Long id,
    String name,
    String locationName,
    LocalDateTime startTime,
    LocalDateTime endTime
) {

    public static TourLogDto of(TourLog entity) {
        return new TourLogDto(
            entity.getId(),
            entity.getName(),
            entity.getLocationName(),
            entity.getStartTime(),
            entity.getEndTime()
        );
    }

    public TourLog toEntity(Member member) {
        return TourLog.of(
            name,
            locationName,
            member,
            startTime,
            endTime
        );
    }

}