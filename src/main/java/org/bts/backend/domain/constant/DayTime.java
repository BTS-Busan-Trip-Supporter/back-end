package org.bts.backend.domain.constant;

import java.time.LocalTime;
import lombok.Getter;

@Getter
public enum DayTime {
    MORNING(LocalTime.of(9, 0), LocalTime.of(11, 59)),
    MIDNOON(LocalTime.of(12, 0), LocalTime.of(13, 59)),
    AFTERNOON(LocalTime.of(16, 0), LocalTime.of(19, 59)),
    EVENING(LocalTime.of(20, 0), LocalTime.of(23, 59));

    private final LocalTime startTime;
    private final LocalTime endTime;

    DayTime(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
