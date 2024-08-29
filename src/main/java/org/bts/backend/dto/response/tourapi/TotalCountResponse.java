package org.bts.backend.dto.response.tourapi;

import java.util.List;

public record TotalCountResponse(
    Response<TotalCountBody> response
) {
    public record TotalCountBody(
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
        int totalCnt
    ) {

    }

    private static final int FIRST_IDX = 0;

    public int getTotalCount() {
        return this.response.body().items.item.get(FIRST_IDX).totalCnt;
    }
}
