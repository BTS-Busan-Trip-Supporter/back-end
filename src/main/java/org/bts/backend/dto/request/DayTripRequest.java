package org.bts.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import org.bts.backend.domain.constant.DayTime;

@Schema(description = "당일치기 여행 추천 요청 DTO [참조 figma #4]")
public record DayTripRequest(
    @Schema(description = "관광 타입", allowableValues = {"12", "14", "15", "28", "38", "39"})
    String contentTypeId,
    @Schema(description = "부산광역시의 시/군/구 코드", allowableValues = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"})
    String sigunguCode,
    @Schema(description = "언제 가시나요", allowableValues = {"MORNING", "AFTERNOON", "EVENING", "NIGHT"})
    List<DayTime> dayTimes,
    @Schema(description = "여행 일자")
    LocalDate tourDate
) {

}
