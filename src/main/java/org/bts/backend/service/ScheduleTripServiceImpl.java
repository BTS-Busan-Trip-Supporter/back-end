package org.bts.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.bts.backend.api.tmap.TmapAPI;
import org.bts.backend.api.tmap.TmapResponse;
import org.bts.backend.domain.Member;
import org.bts.backend.domain.TourActivity;
import org.bts.backend.domain.TourLog;
import org.bts.backend.domain.TourSpot;
import org.bts.backend.dto.TourActivityDto;
import org.bts.backend.dto.TourLogDto;
import org.bts.backend.dto.response.ScheduleTripResponse;
import org.bts.backend.dto.response.ScheduleTripsResponse;
import org.bts.backend.repository.MemberRepository;
import org.bts.backend.repository.TourActivityRepository;
import org.bts.backend.repository.TourLogRepository;
import org.bts.backend.repository.TourSpotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ScheduleTripServiceImpl implements ScheduleTripService {

    private final MemberRepository memberRepository;
    private final TourLogRepository tourLogRepository;
    private final TourActivityRepository tourActivityRepository;
    private final TourSpotRepository tourSpotRepository;

    private final TmapAPI tmapAPI;

    @Override
    @Transactional
    public Long saveScheduleTrip(String email, TourLogDto tourLogDto, List<TourActivityDto> tourActivityDtoList) {
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
        return tourLog.getId();
    }

    @Override
    public ScheduleTripResponse getScheduleTrip(Long id) {
        TourLog tourLogWithActivities = tourLogRepository.findByIdWithTourActivities(id);

        List<TourActivity> tourActivities = tourLogWithActivities.getTourActivities();
        List<TourActivityDto> tourActivityDtoResult = new ArrayList<>();

        for(int i=0; i<tourActivities.size(); i++) {
            String startX = String.valueOf(tourActivities.get(i).getTourSpot().getMapX());
            String startY = String.valueOf(tourActivities.get(i).getTourSpot().getMapY());

            String endX = null;
            String endY = null;

            if(i < tourActivities.size() - 1) {
                endX = String.valueOf(tourActivities.get(i + 1).getTourSpot().getMapX());
                endY = String.valueOf(tourActivities.get(i + 1).getTourSpot().getMapY());
            }

            TmapResponse tmapResponse = null;
            if(i < tourActivities.size() - 1) {
                tmapResponse = tmapAPI.requestTmapAPI(startX, startY, endX, endY).block();
            }

            Integer totalTime;
            if(tmapResponse == null
                || tmapResponse.getMetaData() == null
                || tmapResponse.getMetaData().getPlan() == null
                || tmapResponse.getMetaData().getPlan().getItineraries() == null) {
                totalTime = null;
            } else {
                totalTime = tmapResponse.getMetaData().getPlan().getItineraries().get(0).getTotalTime();
            }
            tourActivityDtoResult.add(
                TourActivityDto.of(
                    tourActivities.get(i),
                    totalTime
                )
            );
        }

        return new ScheduleTripResponse(
            TourLogDto.of(tourLogWithActivities),
            tourActivityDtoResult
        );
    }

    @Override
    public ScheduleTripsResponse getAllScheduleTrips(String email) {
        List<TourLog> tourLogs = tourLogRepository.findAllByMemberEmail(email);

        return new ScheduleTripsResponse(
            tourLogs
                .stream()
                .map(TourLogDto::of)
                .toList()
        );
    }

    @Override
    @Transactional
    public void updateScheduleTrip(Long id, TourLogDto tourLogDto, List<TourActivityDto> tourActivityDtoList) {
        TourLog tourLog = tourLogRepository
            .findById(id)
            .orElseThrow(IllegalArgumentException::new);

        for(TourActivityDto tourActivityDto : tourActivityDtoList) {
            // 새로운 장소 추가
            if(tourActivityDto.id() == null) {
                TourSpot tourSpot = tourSpotRepository
                    .findById(tourActivityDto.tourSpotDto().id())
                    .orElseThrow(IllegalArgumentException::new);

                TourActivity tourActivity = tourActivityDto.toEntity(tourLog, tourSpot);
                tourActivityRepository.save(tourActivity);
                tourLog.addTourActivity(tourActivity);
                continue;
            }

            TourActivity tourActivity = tourActivityRepository
                .findById(tourActivityDto.id())
                .orElseThrow(IllegalArgumentException::new);

            // 기존 장소 삭제
            if(tourActivityDto.isDeleted()) {
                // 기존 순서 변경하는 쿼리문 (-1)
                tourActivityRepository.decrementOrderIndexFrom(
                    id,
                    tourActivity.getDayNumber(),
                    tourActivity.getDayTime(),
                    tourActivity.getOrderIndex()
                );
                tourActivityRepository.delete(tourActivity);
                continue;
            }

            if(Boolean.TRUE.equals(tourActivityDto.isOrderChanged())) {
                // 기존 순서 변경하는 쿼리문 (-1)
                tourActivityRepository.decrementOrderIndexFrom(
                    id,
                    tourActivity.getDayNumber(),
                    tourActivity.getDayTime(),
                    tourActivity.getOrderIndex()
                );
                // 새로운 순서 변경하는 쿼리문 (+1)
                tourActivityRepository.incrementOrderIndexFrom(
                    id,
                    tourActivityDto.dayNumber(),
                    tourActivityDto.dayTime(),
                    tourActivityDto.orderIndex()
                );
            }

            tourActivity.updateSpotName(tourActivityDto.spotName());
            tourActivity.updateDayTime(tourActivityDto.dayTime());
            tourActivity.updateOrderIndex(tourActivityDto.orderIndex());
        }
    }

    @Override
    @Transactional
    public void deleteScheduleTrip(Long id) {
        TourLog tourLog = tourLogRepository
            .findById(id)
            .orElseThrow(IllegalArgumentException::new);
        tourLogRepository.delete(tourLog);
    }

    @Override
    @Transactional
    public void recommendTourActivity(Long id, Boolean recommend) {
        TourActivity tourActivity = tourActivityRepository
            .findById(id)
            .orElseThrow(IllegalArgumentException::new);
        tourActivity.updateRecommend(recommend);
    }

    @Override
    @Transactional
    public void updateTourActivityHistory(Long id, String history) {
        TourActivity tourActivity = tourActivityRepository
            .findById(id)
            .orElseThrow(IllegalArgumentException::new);

        tourActivity.updateHistory(history);
    }
}
