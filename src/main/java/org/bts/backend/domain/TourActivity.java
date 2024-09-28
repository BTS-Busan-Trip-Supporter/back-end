package org.bts.backend.domain;

import jakarta.persistence.*;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bts.backend.domain.constant.DayTime;
import org.bts.backend.dto.TourLogDto;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TourActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String spotName; // 관광지 명

    private Boolean recommend;

    private String history;

    @Column(nullable = false)
    private Integer dayNumber; // N 일차

    @Enumerated
    @Column(nullable = false)
    private DayTime dayTime;

    @Column(nullable = false)
    private Integer orderIndex; // dayTime 이 같을 때 오더링

    // TODO: 이미지 데이터

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_spot_id", nullable = false)
    private TourSpot tourSpot;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tour_log_id", nullable = false)
    private TourLog tourLog;

    // -- 생성자 메서드 -- //
    private TourActivity(
        String spotName,
        Integer dayNumber,
        TourSpot tourSpot,
        TourLog tourLog,
        DayTime dayTime,
        Integer orderIndex
    ) {
        this.spotName = spotName;
        this.dayNumber = dayNumber;
        this.tourSpot = tourSpot;
        this.tourLog = tourLog;
        this.dayTime = dayTime;
        this.orderIndex = orderIndex;
    }

    public static TourActivity of(
        String spotName,
        Integer dayNumber,
        TourSpot tourSpot,
        TourLog tourLog,
        DayTime dayTime,
        Integer orderIndex
    ) {
        return new TourActivity(spotName, dayNumber, tourSpot, tourLog, dayTime, orderIndex);
    }

    // -- setter 메서드 -- //
    public void updateSpotName(String spotName) {
        this.spotName = spotName;
    }

    public void updateDayTime(DayTime dayTime) {
        this.dayTime = dayTime;
    }

    public void updateOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public void updateRecommend(Boolean state) {
        if(Objects.equals(recommend, state)) {
            return;
        }

        if (state == null) {
          if(Boolean.TRUE.equals(recommend)) {
              tourSpot.decreaseLikeCount();
          } else {
              tourSpot.decreaseDislikeCount();
          }
        } else if(Boolean.TRUE.equals(state)) {
            tourSpot.increaseLikeCount();
            if(recommend != null) {
                tourSpot.decreaseDislikeCount();
            }
        } else {
            tourSpot.increaseDislikeCount();
            if(recommend != null) {
                tourSpot.decreaseLikeCount();
            }
        }

        recommend = state;
    }

    public void updateHistory(String history) {
        this.history = history;
    }

    public void updateTourLog(TourLog tourLog) {
        this.tourLog = tourLog;
    }
}
