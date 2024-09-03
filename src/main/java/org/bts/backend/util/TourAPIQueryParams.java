package org.bts.backend.util;

import java.util.Map;
import java.util.function.Consumer;
import org.springframework.web.util.UriBuilder;

public class TourAPIQueryParams {

    private static final Map<String, String> COMMON_PARAMS = Map.of(
        "MobileOS", "ETC",
        "MobileApp", "MobileApp",
        "_type", "json"
    );

    public static Consumer<UriBuilder> addCommonParams(String serviceKey) {
        return uriBuilder -> {
            COMMON_PARAMS.forEach(uriBuilder::queryParam);
            uriBuilder.queryParam("serviceKey", serviceKey);
        };
    }
}
