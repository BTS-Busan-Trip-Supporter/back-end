package org.bts.backend.service;

import java.util.List;
import org.bts.backend.domain.constant.DayTime;
import org.bts.backend.dto.response.DayTripResponse;

public interface DayTripService {

    List<DayTripResponse> recommendDayTourSpots(String contentTypeId, String sigunguCode, List<DayTime> dayTimes);

}
