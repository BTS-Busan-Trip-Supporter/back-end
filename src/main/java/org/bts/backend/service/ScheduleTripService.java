package org.bts.backend.service;

import java.util.List;
import org.bts.backend.dto.TourActivityDto;
import org.bts.backend.dto.TourLogDto;
import org.bts.backend.dto.response.ScheduleTripResponse;
import org.bts.backend.dto.response.ScheduleTripsResponse;

public interface ScheduleTripService {

    Long saveScheduleTrip(String email, TourLogDto tourLogDto, List<TourActivityDto> tourActivityDtoList);
    ScheduleTripResponse getScheduleTrip(Long id);
    ScheduleTripsResponse getAllScheduleTrips(String email);
    void updateScheduleTrip(Long id, TourLogDto tourLogDto, List<TourActivityDto> tourActivityDtoList);
    void deleteScheduleTrip(Long id);
    void recommendTourActivity(Long id, Boolean recommend);
    void updateTourActivityHistory(Long id, String history);
}
