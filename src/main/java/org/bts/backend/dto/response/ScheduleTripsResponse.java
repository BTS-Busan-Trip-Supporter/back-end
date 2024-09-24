package org.bts.backend.dto.response;

import java.util.List;
import org.bts.backend.dto.TourLogDto;

public record ScheduleTripsResponse(
    List<TourLogDto> tourLogInfos
) {

}
