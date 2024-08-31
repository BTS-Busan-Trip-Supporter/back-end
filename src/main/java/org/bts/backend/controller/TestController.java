package org.bts.backend.controller;

import lombok.RequiredArgsConstructor;
import org.bts.backend.api.tmap.TmapAPI;
import org.bts.backend.api.tmap.TmapResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor

// THIS IS NOT FOR SERVICE

public class TestController {

    private final TmapAPI tmapAPI;

    @GetMapping("/tmap/test")
    public void test() {

        // 시작, 도착의 위치를 지정.
        Mono<TmapResponse> res = tmapAPI.requestTmapAPI("126.926493082645", "37.6134436427887", "127.126936754911", "37.5004198786564");

        // Subscribe를 통해 Mono의 결과를 출력.
        res.subscribe(tmapResponse -> {
            System.out.println("Start X : "+tmapResponse.getMetaData().getRequestParameters().getStartX());
            System.out.println("Start Y : "+tmapResponse.getMetaData().getRequestParameters().getStartY());
            System.out.println("End X : "+tmapResponse.getMetaData().getRequestParameters().getEndX());
            System.out.println("End Y : "+tmapResponse.getMetaData().getRequestParameters().getEndY());
            System.out.println("Path Type : "+tmapResponse.getMetaData().getPlan().getItineraries().get(0).getPathType());
            System.out.println("Transfer Count : "+tmapResponse.getMetaData().getPlan().getItineraries().get(0).getTransferCount());
            System.out.println("Total Time : "+tmapResponse.getMetaData().getPlan().getItineraries().get(0).getTotalTime());
            System.out.println("Walk Time : "+tmapResponse.getMetaData().getPlan().getItineraries().get(0).getTotalWalkTime());
            System.out.println("Total Distance : "+tmapResponse.getMetaData().getPlan().getItineraries().get(0).getTotalDistance());
            System.out.println("Walk Distance : "+tmapResponse.getMetaData().getPlan().getItineraries().get(0).getTotalWalkDistance());
        });
    }
}
