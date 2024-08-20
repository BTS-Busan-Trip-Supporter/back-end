package org.bts.backend.domain;

import jakarta.persistence.*;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TourActivity extends StartEndTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String spotName; // 관광지 명

    private Boolean recommend;

    // TODO: 이미지 데이터

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_spot_id", nullable = false)
    private TourSpot tourSpot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_day_id", nullable = false)
    private TourDay tourDay;

    // -- 생성자 메서드 -- //
    private TourActivity(String spotName, TourSpot tourSpot, TourDay tourDay) {
        this.spotName = spotName;
        this.tourSpot = tourSpot;
        this.tourDay = tourDay;
    }

    public static TourActivity of(String spotName, TourSpot tourSpot, TourDay tourDay) {
        return new TourActivity(spotName, tourSpot, tourDay);
    }

    // -- setter 메서드 -- //
    public void updateRecommend(boolean state) {
        if(Objects.equals(recommend, state)) {
            return;
        }

        if(state) {
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

}
