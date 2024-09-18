package org.bts.backend.service;

import java.util.List;
import org.bts.backend.dto.TourActivityDto;
import org.bts.backend.dto.TourLogDto;
import org.bts.backend.dto.response.ScheduleTripResponse;

public interface ScheduleTripService {

    void saveScheduleTrip(String email, TourLogDto tourLogDto, List<TourActivityDto> tourActivityDtoList);

    ScheduleTripResponse getScheduleTrip(Long id);
}
