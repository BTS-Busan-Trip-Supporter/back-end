package org.bts.backend.dto.response.tourapi;

import java.util.List;

public record DetailCommonResponse(
    Response<DetailCommonBody> response
) {

    public record DetailCommonBody(
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
        String contentid,
        String contenttypeid,
        String title,
        String createdtime,
        String modifiedtime,
        String tel,
        String telname,
        String homepage,
        String booktour,
        String firstimage,
        String firstimage2,
        String cpyrhtDivCd,
        String areacode,
        String sigungucode,
        String cat1,
        String cat2,
        String cat3,
        String addr1,
        String addr2,
        String zipcode,
        String mapx,
        String mapy,
        String mlevel,
        String overview
    ) {

    }
}
