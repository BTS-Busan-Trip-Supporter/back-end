package org.bts.backend.service;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.bts.backend.dto.response.TourSpotDetailResponse;
import org.bts.backend.dto.response.tourapi.DetailCommonResponse;
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
}
