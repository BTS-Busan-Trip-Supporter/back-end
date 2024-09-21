package org.bts.backend.service;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.bts.backend.dto.response.TourSpotDetailResponse;
import org.bts.backend.dto.response.TourSpotResponse;
import org.bts.backend.dto.response.tourapi.DetailCommonResponse;
import org.bts.backend.dto.response.tourapi.SearchKeywordResponse;
import org.bts.backend.dto.response.tourapi.SearchKeywordResponse.Item;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TourSpotServiceImpl implements TourSpotService {

    private final TourAPIService tourAPIService;

    @Override
    public TourSpotDetailResponse getTourSpotDetailResponse(
        String contentId,
        String contentTypeId
    ) {
        DetailCommonResponse detailCommonResponse = tourAPIService
            .getDetailCommonResponse(
                contentId,
                Map.of(
                    "contentTypeId", contentTypeId,
                    "defaultYN", "Y",
                    "firstImageYN", "Y",
                    "addrinfoYN", "Y",
                    "mapinfoYN", "Y",
                    "overviewYN", "Y"
                ))
            .block();

        assert detailCommonResponse != null;
        return TourSpotDetailResponse.of(detailCommonResponse);
    }

    @Override
    public TourSpotResponse getTourSpotResponse(
        String keyword,
        String sigunguCode
    ) {
        SearchKeywordResponse searchKeywordResponse = tourAPIService
            .getSearchKeywordResponse(
                keyword,
                Map.of(
                    "areaCode", "6",
                    "sigunguCode", sigunguCode
                )
            )
            .block();

        // TODO: 예외처리 구체화하기
        assert searchKeywordResponse != null;
        Item item = searchKeywordResponse
            .getItem()
            .stream()
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);

        return new TourSpotResponse(
            item.contentid(),
            item.contenttypeid(),
            item.title(),
            item.sigungucode()
        );
    }


}
