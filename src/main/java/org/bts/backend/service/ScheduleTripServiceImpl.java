package org.bts.backend.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bts.backend.domain.Member;
import org.bts.backend.domain.TourActivity;
import org.bts.backend.domain.TourLog;
import org.bts.backend.domain.TourSpot;
import org.bts.backend.dto.TourActivityDto;
import org.bts.backend.dto.TourLogDto;
import org.bts.backend.dto.response.ScheduleTripResponse;
import org.bts.backend.repository.MemberRepository;
import org.bts.backend.repository.TourActivityRepository;
import org.bts.backend.repository.TourLogRepository;
import org.bts.backend.repository.TourSpotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleTripServiceImpl implements ScheduleTripService {

    private final MemberRepository memberRepository;
    private final TourLogRepository tourLogRepository;
    private final TourActivityRepository tourActivityRepository;
    private final TourSpotRepository tourSpotRepository;

    @Override
    @Transactional
    public void saveScheduleTrip(String email, TourLogDto tourLogDto, List<TourActivityDto> tourActivityDtoList) {
        Member member = memberRepository
            .findByEmail(email)
            .orElseThrow(IllegalArgumentException::new);

        TourLog tourLog = tourLogDto.toEntity(member);
        tourLog = tourLogRepository.save(tourLog);

        for(TourActivityDto tourActivityDto : tourActivityDtoList) {
            TourSpot tourSpot = tourSpotRepository
                .findById(tourActivityDto.tourSpotDto().id())
                .orElseThrow(IllegalArgumentException::new);

            TourActivity tourActivity = tourActivityDto.toEntity(tourLog, tourSpot);
            tourActivityRepository.save(tourActivity);
        }
    }

    @Override
    public ScheduleTripResponse getScheduleTrip(Long id) {
        TourLog tourLogWithActivities = tourLogRepository.findByIdWithTourActivities(id);

        return new ScheduleTripResponse(
            TourLogDto.of(tourLogWithActivities),
            tourLogWithActivities
                .getTourActivities()
                .stream()
                .map(TourActivityDto::of)
                .toList()
        );
    }


}
