package org.bts.backend.service;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.bts.backend.dto.response.tourapi.LocationBasedResponse;
import org.bts.backend.util.TourAPIQueryParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TourAPIServiceImpl implements TourAPIService {

    @Value("${tour_api.service_key}")
    private String serviceKey;

    @Value("${tour_api.host}")
    private String host;

    @Value("${tour_api.base_path}")
    private String basePath;

    public WebClient getTourApiClient() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        return WebClient.builder()
                .uriBuilderFactory(factory)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.ALL_VALUE);
                })
                .build();
    }

    public Mono<LocationBasedResponse> getLocationBasedResponse(
        String mapX,
        String mapY,
        String radius,
        Map<String, String> additionalParams
    ) {
        WebClient webClient = getTourApiClient();

        return webClient.get()
            .uri(uriBuilder -> {
                uriBuilder
                    .scheme("https")
                    .host(host)
                    .path(basePath + "/locationBasedList1");
                // 기본적인 쿼리 파라미터 값 추가
                TourAPIQueryParams.addCommonParams(serviceKey).accept(uriBuilder);
                // required 쿼리 파리미터 값 추가
                uriBuilder
                    .queryParam("mapX", mapX)
                    .queryParam("mapY", mapY)
                    .queryParam("radius", radius);
                // 추가적인 쿼리 파라미터 값 추가
                if (additionalParams != null) {
                    additionalParams.forEach(uriBuilder::queryParam);
                }
                return uriBuilder.build();
            })
            .retrieve()
            .bodyToMono(LocationBasedResponse.class);
    }
}
