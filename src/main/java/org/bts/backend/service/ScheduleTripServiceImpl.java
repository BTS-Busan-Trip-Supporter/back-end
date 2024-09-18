package org.bts.backend.service;

import lombok.RequiredArgsConstructor;
import org.bts.backend.dto.TourActivityDto;
import org.bts.backend.dto.TourLogDto;
import org.bts.backend.repository.TourActivityRepository;
import org.bts.backend.repository.TourLogRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleTripServiceImpl implements ScheduleTripService {

    private final TourLogRepository tourLogRepository;
    private final TourActivityRepository tourActivityRepository;

    @Override
    public void saveScheduleTrip(TourLogDto tourLogDto, TourActivityDto tourActivityDto) {

    }
}
