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

        // TODO: 각 관광지 별 대중교통 시간도 추가하기

        return new ScheduleTripResponse(
            TourLogDto.of(tourLogWithActivities),
            tourLogWithActivities
                .getTourActivities()
                .stream()
                .map(TourActivityDto::of)
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
