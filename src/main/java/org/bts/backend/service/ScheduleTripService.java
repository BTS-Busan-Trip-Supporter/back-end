package org.bts.backend.service;

import org.bts.backend.dto.TourActivityDto;
import org.bts.backend.dto.TourLogDto;

public interface ScheduleTripService {

    void saveScheduleTrip(TourLogDto tourLogDto, TourActivityDto tourActivityDto);
}
