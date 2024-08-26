package org.bts.backend.api.tmap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class TmapAPI {

    @Value("${tmap.key}")
    private String tmapApiKey;

    @Value("${tmap.url}")
    private String tmapApiUrl;

    public WebClient getTmapClient(){
        return WebClient.builder()
            .baseUrl(tmapApiUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader("appKey", tmapApiKey)
            .build();
    }

    public Mono<TmapResponse> RequestTmapAPI(String startX, String startY, String endX, String endY){
        WebClient client = getTmapClient();
        String requestBody = "{\n" +
            "  \"startX\": \"" + startX + "\",\n" +
            "  \"startY\": \"" + startY + "\",\n" +
            "  \"endX\": \"" + endX + "\",\n" +
            "  \"endY\": \"" + endY + "\"\n" +
            "}";

        return client.post()
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(TmapResponse.class);

    }
}
