package org.bts.backend.dto.response.tourapi;

import java.util.List;

public record LocationBasedResponse(
    Response<LocationBody> response
) {
    public record LocationBody(
        Items items,
        int numOfRows,
        int pageNo,
        int totalCount
    ) {

    }

    public record Items(
        List<Item> item
    ) {

    }

    public record Item(
        String addr1,
        String addr2,
        String areacode,
        String booktour,
        String cat1,
        String cat2,
        String cat3,
        String contentid,
        String contenttypeid,
        String createdtime,
        String dist,
        String firstimage,
        String firstimage2,
        String cpyrhtDivCd,
        String mapx,
        String mapy,
        String mlevel,
        String modifiedtime,
        String sigungucode,
        String tel,
        String title
    ) {

    }
}
