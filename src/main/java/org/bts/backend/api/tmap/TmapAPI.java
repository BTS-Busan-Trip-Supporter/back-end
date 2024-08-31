package org.bts.backend.api.tmap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class TmapAPI {

    @Value("${tmap.key}")
    private String tmapApiKey;

    @Value("${tmap.url}")
    private String tmapApiUrl;

    // WebClient 빌더를 통해 Tmap API에 대한 WebClient를 생성
    public WebClient getTmapClient(){
        return WebClient.builder()
            .baseUrl(tmapApiUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader("appKey", tmapApiKey)
            .build();
    }


    // Tmap API에 대한 요청을 보내는 메소드
    public Mono<TmapResponse> requestTmapAPI(String startX, String startY, String endX, String endY){
        WebClient client = getTmapClient();
        String requestBody = "{\n" +
            "  \"startX\": \"" + startX + "\",\n" +
            "  \"startY\": \"" + startY + "\",\n" +
            "  \"endX\": \"" + endX + "\",\n" +
            "  \"endY\": \"" + endY + "\",\n" +
            "  \"count\": \"" + 1 + "\"\n"+
            "}";

        return client.post()
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(TmapResponse.class);
    }
}
