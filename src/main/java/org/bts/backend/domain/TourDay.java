package org.bts.backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TourDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int dayNumber; // N일차

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_log_id", nullable = false)
    private TourLog tourLog;

    // -- 생성자 메서드 -- //
    private TourDay(int dayNumber, TourLog tourLog) {
        this.dayNumber = dayNumber;
        this.tourLog = tourLog;
    }

    public static TourDay of(int dayNumber, TourLog tourLog) {
        return new TourDay(dayNumber, tourLog);
    }

}
