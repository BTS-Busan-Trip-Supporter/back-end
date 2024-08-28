package org.bts.backend.service;

import lombok.RequiredArgsConstructor;
import org.bts.backend.dto.response.tourapi.LocationBasedResponse;
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

    public Mono<LocationBasedResponse> getLocationBasedResponse() {
        WebClient webClient = getTourApiClient();

        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .scheme("https")
                .host(host)
                .path(basePath + "/locationBasedList1")
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "MobileApp")
                .queryParam("_type", "json")
                .queryParam("mapX", "128.878492")
                .queryParam("mapY", "37.74913611")
                .queryParam("radius", "1000")
                .queryParam("serviceKey", serviceKey)
                .build()
            )
            .retrieve()
            .bodyToMono(LocationBasedResponse.class);
    }
}
