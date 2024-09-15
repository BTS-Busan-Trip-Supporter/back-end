package org.bts.backend.service;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.bts.backend.domain.TourSpot;
import org.bts.backend.domain.constant.DayTime;
import org.bts.backend.dto.response.DayTripResponse;
import org.bts.backend.dto.response.tourapi.DetailCommonResponse;
import org.bts.backend.dto.response.tourapi.DetailIntroResponse;
import org.bts.backend.repository.TourSpotRepository;
import org.bts.backend.util.RegexUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DayTripServiceImpl implements DayTripService {

    private final TourSpotRepository tourSpotRepository;

    private final TourAPIService tourAPIService;

    @Override
    public List<DayTripResponse> recommendDayTourSpots(String contentTypeId, String sigunguCode, List<DayTime> dayTimes) {
        // TODO: 추천을 기반으로 오더링
        List<TourSpot> tourSpots = tourSpotRepository.findAllByTypeIdAndSigunguCode(
            contentTypeId, sigunguCode);

        return tourSpots.stream()
            .filter(tourSpot -> {
                // 시간 필터링
                DetailIntroResponse detailIntroResponse = tourAPIService
                    .getDetailIntroResponse(
                        tourSpot.getId(),
                        tourSpot.getTypeId(),
                        null
                    )
                    .block();
                LocalTime[] openingHours = RegexUtil.extractOpeningHours(tourSpot.getTypeId(),
                    detailIntroResponse);

                if(openingHours.length == 0) {
                    return true;
                }

                LocalTime startTime = dayTimes.get(0).getStartTime();
                LocalTime endTime = dayTimes.get(dayTimes.size() - 1).getEndTime();

                return !startTime.isAfter(openingHours[1]) && !endTime.isBefore(openingHours[0]);
            })
            .limit(10)
            .map(tourSpot -> {
                // DTO mapping
                DetailCommonResponse detailCommonResponse = tourAPIService
                    .getDetailCommonResponse(
                        tourSpot.getId(),
                        Map.of("firstImageYN", "Y")
                    )
                    .block();

                assert detailCommonResponse != null;
                return DayTripResponse.of(tourSpot.getId(), tourSpot.getTypeId(),
                    tourSpot.getTitle(), detailCommonResponse.getItem().firstimage());
            })
            .toList();
    }
}
