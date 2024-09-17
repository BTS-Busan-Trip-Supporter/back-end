package org.bts.backend.dto.response;

import org.bts.backend.dto.response.tourapi.DetailCommonResponse;
import org.bts.backend.util.RegexUtil;

public record TourSpotDetailResponse(
    String contentId,
    String contentTypeId,
    String title,
    String tel,
    String homepageUrl,
    String firstImage,
    String secondImage,
    String addr1,
    String addr2,
    String mapx,
    String mapy,
    String mlevel,
    String overview
) {

    public static TourSpotDetailResponse of(DetailCommonResponse response) {
        return new TourSpotDetailResponse(
            response.getItem().contentid(),
            response.getItem().contenttypeid(),
            response.getItem().title(),
            response.getItem().tel(),
            RegexUtil.extractHomepageURL(response.getItem().homepage()),
            response.getItem().firstimage(),
            response.getItem().firstimage2(),
            response.getItem().addr1(),
            response.getItem().addr2(),
            response.getItem().mapx(),
            response.getItem().mapy(),
            response.getItem().mlevel(),
            response.getItem().overview()
        );
    }

}
