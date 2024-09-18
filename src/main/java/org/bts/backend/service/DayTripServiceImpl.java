package org.bts.backend.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.bts.backend.domain.TourSpot;
import org.bts.backend.domain.constant.DayTime;
import org.bts.backend.dto.response.DayTripResponse;
import org.bts.backend.dto.response.tourapi.DetailCommonResponse;
import org.bts.backend.dto.response.tourapi.DetailIntroResponse;
import org.bts.backend.exception.after_servlet.NoTourSpotsFoundException;
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
        // Step 1: tourSpots 데이터 가져오기
        List<TourSpot> tourSpots = tourSpotRepository.findAllByTypeIdAndSigunguCode(contentTypeId, sigunguCode);

        System.out.println("tourSpots: " + tourSpots);

        // Step 2: 평점 비율 계산 (likeCount와 dislikeCount 기반)
        List<Double> ratingRatios = new ArrayList<>();
        for (TourSpot tourSpot : tourSpots) {
            double totalVotes = tourSpot.getLikeCount() + tourSpot.getDislikeCount();
            double ratingRatio = totalVotes == 0 ? 0 : (double) tourSpot.getLikeCount() / totalVotes;
            ratingRatios.add(ratingRatio);
        }

        // Step 3: 영업 시간 필터링 추가
        List<TourSpot> filteredTourSpots = new ArrayList<>();
        List<Double> filteredRatingRatios = new ArrayList<>();

        for (int i = 0; i < tourSpots.size(); i++) {
            TourSpot tourSpot = tourSpots.get(i);
            double ratingRatio = ratingRatios.get(i);

            // 시간 필터링
            DetailIntroResponse detailIntroResponse = tourAPIService
                    .getDetailIntroResponse(tourSpot.getId(), tourSpot.getTypeId(), null)
                    .block();

            LocalTime[] openingHours = RegexUtil.extractOpeningHours(tourSpot.getTypeId(), detailIntroResponse);
            if (openingHours.length == 0 || (!dayTimes.get(0).getStartTime().isAfter(openingHours[1]) && !dayTimes.get(dayTimes.size() - 1).getEndTime().isBefore(openingHours[0]))) {
                filteredTourSpots.add(tourSpot);
                filteredRatingRatios.add(ratingRatio);
            }
        }

        // 빈 리스트 처리: 만약 필터링 후 관광지가 없으면 기본 응답 처리
        if (filteredTourSpots.isEmpty()) {
            throw new NoTourSpotsFoundException();
        }

        // Step 4: 평점 비율을 기반으로 가중치 계산
        double totalRatingRatio = filteredRatingRatios.stream().mapToDouble(Double::doubleValue).sum();

        List<Double> probabilities = new ArrayList<>();
        for (double ratingRatio : filteredRatingRatios) {
            probabilities.add(ratingRatio / totalRatingRatio);
        }

        // Step 5: 가중치에 따른 랜덤 추천 (확률 기반) - 10개
        List<TourSpot> selectedSpots = new ArrayList<>();
        while (selectedSpots.size() < 10 && !filteredTourSpots.isEmpty()) {
            double randomValue = Math.random();
            double cumulativeProbability = 0.0;

            TourSpot selectedSpot = null;
            for (int i = 0; i < filteredTourSpots.size(); i++) {
                cumulativeProbability += probabilities.get(i);
                if (randomValue <= cumulativeProbability) {
                    selectedSpot = filteredTourSpots.get(i);
                    selectedSpots.add(selectedSpot);

                    // 선택된 관광지는 다시 추천되지 않도록 제거
                    filteredTourSpots.remove(i);
                    probabilities.remove(i);
                    break;
                }
            }

            // 확률 배열 업데이트 (비율 재계산)
            if (!filteredTourSpots.isEmpty()) {
                totalRatingRatio = probabilities.stream().mapToDouble(Double::doubleValue).sum();
                for (int i = 0; i < probabilities.size(); i++) {
                    probabilities.set(i, probabilities.get(i) / totalRatingRatio);
                }
            }
        }

        // Step 6: 선택된 관광지들의 정보를 응답으로 매핑
        List<DayTripResponse> responses = new ArrayList<>();
        for (TourSpot spot : selectedSpots) {
            DetailCommonResponse detailCommonResponse = tourAPIService
                    .getDetailCommonResponse(spot.getId(), Map.of("firstImageYN", "Y"))
                    .block();

            responses.add(DayTripResponse.of(
                    spot.getId(),
                    spot.getTypeId(),
                    spot.getTitle(),
                    detailCommonResponse.getItem().firstimage()
            ));
        }

        return responses;
    }
}
