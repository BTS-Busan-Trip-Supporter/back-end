package org.bts.backend.domain.constant;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum ContentType {
    TOURIST_ATTRACTION(12, "관광지"),
    CULTURAL_FACILITY(14, "문화시설"),
    FESTIVAL_EVENT(15, "축제공연행사"),
    LEISURE_SPORTS(28, "레포츠"),
    SHOPPING(38, "쇼핑"),
    RESTAURANT(39, "음식점");

    private final int code;
    private final String description;

    ContentType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ContentType of(int code) {
        return Arrays.stream(ContentType.values())
                     .filter(ct -> ct.getCode() == code)
                     .findAny()
                     .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관광타입입니다."));
    }
}
