package org.bts.backend.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.bts.backend.domain.TourSpot;
import org.bts.backend.dto.response.tourapi.DetailCommonResponse;
import org.bts.backend.dto.response.tourapi.LocationBasedResponse;
import org.bts.backend.dto.response.tourapi.SearchKeywordResponse;
import org.bts.backend.dto.response.tourapi.TotalCountResponse;
import org.bts.backend.repository.TourSpotRepository;
import org.bts.backend.util.TourAPIQueryParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    private static final String SCHEME = "https";
    private static final int NUM_OF_ROWS = 100;

    private final TourSpotRepository tourSpotRepository;

    private WebClient getTourApiClient() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        return WebClient.builder()
                .uriBuilderFactory(factory)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.ALL_VALUE);
                })
                .build();
    }

    @Override
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
                    .scheme(SCHEME)
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

    @Override
    public Mono<SearchKeywordResponse> getSearchKeywordResponse(String keyword, Map<String, String> additionalParams) {
        WebClient webClient = getTourApiClient();

        return webClient.get()
                        .uri(uriBuilder -> {
                            uriBuilder
                                .scheme(SCHEME)
                                .host(host)
                                .path(basePath + "/searchKeyword1");
                            // 기본적인 쿼리 파라미터 값 추가
                            TourAPIQueryParams.addCommonParams(serviceKey).accept(uriBuilder);
                            // required 쿼리 파리미터 값 추가
                            uriBuilder
                                .queryParam("keyword", encodeParam(keyword));
                            // 추가적인 쿼리 파라미터 값 추가
                            if (additionalParams != null) {
                                additionalParams.forEach(uriBuilder::queryParam);
                            }
                            return uriBuilder.build();
                        })
                        .retrieve()
                        .bodyToMono(SearchKeywordResponse.class);
    }

    @Override
    public Mono<DetailCommonResponse> getDetailCommonResponse(String contentId, Map<String, String> additionalParams) {
        WebClient webClient = getTourApiClient();

        return webClient.get()
                        .uri(uriBuilder -> {
                            uriBuilder
                                .scheme(SCHEME)
                                .host(host)
                                .path(basePath + "/searchKeyword1");
                            // 기본적인 쿼리 파라미터 값 추가
                            TourAPIQueryParams.addCommonParams(serviceKey).accept(uriBuilder);
                            // required 쿼리 파리미터 값 추가
                            uriBuilder
                                .queryParam("contentid", contentId);
                            // 추가적인 쿼리 파라미터 값 추가
                            if (additionalParams != null) {
                                additionalParams.forEach(uriBuilder::queryParam);
                            }
                            return uriBuilder.build();
                        })
                        .retrieve()
                        .bodyToMono(DetailCommonResponse.class);
    }

    @Override
    public Mono<TotalCountResponse> getItemsTotalCountResponse(
        String targetPath,
        Map<String, String> requiredParams,
        Map<String, String> additionalParams
    ) {
        WebClient webClient = getTourApiClient();

        return webClient.get()
                        .uri(uriBuilder -> {
                            uriBuilder
                                .scheme(SCHEME)
                                .host(host)
                                .path(basePath + targetPath);
                            // 기본적인 쿼리 파라미터 값 추가
                            TourAPIQueryParams.addCommonParams(serviceKey).accept(uriBuilder);
                            // required 쿼리 파리미터 값 추가
                            if(requiredParams != null) {
                                requiredParams.forEach(uriBuilder::queryParam);
                            }
                            // 추가적인 쿼리 파라미터 값 추가
                            if (additionalParams != null) {
                                additionalParams.forEach(uriBuilder::queryParam);
                            }
                            return uriBuilder.build();
                        })
                        .retrieve()
                        .bodyToMono(TotalCountResponse.class);
    }

    @Override
    @Transactional
    public void saveAllTourData(String location) {
        Map<String, String> requiredParams = Map.of("keyword", encodeParam(location));
        Map<String, String> additionalParams = Map.of("listYN", "N");

        int totalCount = Objects.requireNonNull(
                                    getItemsTotalCountResponse(
                                        "/searchKeyword1", requiredParams,
                                        additionalParams
                                    )
                                        .block()
                                )
                                .getTotalCount();


        for(int i=1; i<=Math.ceil((double) totalCount / NUM_OF_ROWS); i++) {
            List<TourSpot> entities = new ArrayList<>();
            SearchKeywordResponse searchKeywordResponse = getSearchKeywordResponse(
                location, Map.of("numOfRows", String.valueOf(NUM_OF_ROWS))
            )
            .block();

            assert searchKeywordResponse != null;
            searchKeywordResponse.response().body().items().item().forEach(item ->
                entities.add(
                    TourSpot.of(
                        item.contentid(),
                        item.contenttypeid(),
                        item.title()
                    )
                )
            );
            tourSpotRepository.saveAll(entities);
        }
    }

    private String encodeParam(String param) {
        return URLEncoder.encode(param, StandardCharsets.UTF_8);
    }
}
