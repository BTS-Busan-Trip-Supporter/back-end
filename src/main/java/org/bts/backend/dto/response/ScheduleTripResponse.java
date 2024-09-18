package org.bts.backend.dto.response;

import java.util.List;
import org.bts.backend.dto.TourActivityDto;
import org.bts.backend.dto.TourLogDto;

public record ScheduleTripResponse(
    TourLogDto tourLogInfo,
    List<TourActivityDto> tourActivityInfos
) {

}
