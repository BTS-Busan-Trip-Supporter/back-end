package org.bts.backend.dto.request;

import java.time.LocalDate;
import java.util.List;
import org.bts.backend.domain.constant.DayTime;

public record DayTripRequest(
    String contentTypeId,
    String sigunguCode,
    List<DayTime> dayTimes,
    LocalDate tourDate
) {

}
