package org.bts.backend.dto.response.tourapi;

import java.util.List;

public record AreaBasedResponse(
    Response<AreaBasedBody> response
) {

    public record AreaBasedBody(
        Items items,
        int numOfRows,
        int pageNo,
        int totalCount
    ) {

    }

    public record Items(
        List<Item> item
    ){

    }

    public record Item(
        String contentid,
        String contenttypeid,
        String mapx,
        String mapy,
        String mlevel,
        String areacode,
        String sigungucode,
        String title
    ) {

    }

}
