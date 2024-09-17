package org.bts.backend.dto.response.tourapi;

import java.util.List;
import org.bts.backend.domain.constant.ContentType;

public record DetailIntroResponse(
    Response<DetailIntroBody> response
) {

    public record DetailIntroBody(
        Items items,
        int numOfRows,
        int pageNo,
        int totalCount
    ) { }

    public record Items(
        List<Item> item
    ) { }

    public record Item(
        String useseason, // (12) 이용 시기
        String usetime, // (12) 이용 시간
        String opendate, // (12) 개장일
        String restdate, // (12, 39) 쉬는 날
        String restdateculture, // (14) 쉬는 날
        String usetimeculture, // (14) 이용 시간
        String spendtime, // (14) 관람 소요 시간
        String eventstartdate, // (15) 행사 시작일
        String eventenddate, // (15) 행사 종료일
        String playtime, // (15) 공연 시간
        String spendtimefestival, // (15) 관람 소요 시간
        String openperiod, // (28) 개장 시간
        String usetimeleports, // (28) 이용 시간
        String restdateleports, // (28) 쉬는 날
        String fairday, // (38) 장서는 날
        String opendateshopping, // (38) 개장일
        String opentime, // (38) 영업 시간
        String restdateshopping, // (38) 쉬는 날
        String opentimefood, // (39) 영업 시간
        String restdatefood // (39) 쉬는 날
    ) { }

    private static final int FIRST_IDX = 0;
    private Item getItem() {
        return this.response.body().items.item.get(FIRST_IDX);
    }

    public String getOpeningTime(ContentType contentType) {
        switch (contentType) {
            case TOURIST_ATTRACTION -> {
                return getItem().usetime();
            }
            case CULTURAL_FACILITY -> {
                return getItem().usetimeculture();
            }
            case FESTIVAL_EVENT -> {
                return getItem().playtime();
            }
            case LEISURE_SPORTS -> {
                return getItem().usetimeleports();
            }
            case SHOPPING -> {
                return getItem().opentime();
            }
            case RESTAURANT -> {
                return getItem().opentimefood();
            }
            default -> {
                return null;
            }
        }
    }
}
