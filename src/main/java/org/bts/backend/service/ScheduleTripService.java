package org.bts.backend.service;

import java.util.List;
import org.bts.backend.dto.TourActivityDto;
import org.bts.backend.dto.TourLogDto;

public interface ScheduleTripService {

    void saveScheduleTrip(String email, TourLogDto tourLogDto, List<TourActivityDto> tourActivityDtoList);
}
