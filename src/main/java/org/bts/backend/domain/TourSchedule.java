package org.bts.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TourSchedule extends StartEndTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String spotName; // 관광지 명

    @ColumnDefault("true")
    private Boolean recommend;

    @Column(length = 300)
    private String review;

    // TODO: 이미지 데이터

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_log_id", nullable = false)
    private TourLog tourLog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tourist_spot_id", nullable = false)
    private TouristSpot touristSpot;

    // -- 생성자 메서드 -- //
    private TourSchedule(String spotName, TourLog tourLog, TouristSpot touristSpot) {
        this.spotName = spotName;
        this.tourLog = tourLog;
        this.touristSpot = touristSpot;
    }

    public static TourSchedule of(String spotName, TourLog tourLog, TouristSpot touristSpot) {
        return new TourSchedule(spotName, tourLog, touristSpot);
    }

    // -- setter 메서드 -- //
    public void toggleRecommend() {
        this.recommend = !this.recommend;

        if(Boolean.TRUE.equals(this.recommend)) {
            this.touristSpot.increaseLikeCount();
        } else {
            this.touristSpot.decreaseLikeCount();
        }
    }

    public void writeReview(String review) {
        this.review = review;
    }
}
