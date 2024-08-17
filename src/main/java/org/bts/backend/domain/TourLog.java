package org.bts.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TourLog extends StartEndTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 여행 이름 ex) 소소한 부산 여행

    @Column(nullable = false)
    private String locationName; // 여행 갈 지역명 ex) 부산

    // TODO: 이미지 데이터

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // -- 생성자 메서드 -- //
    private TourLog(
        String name,
        String locationName,
        Member member,
        LocalDateTime startTime,
        LocalDateTime endTime
    ) {
        super(startTime, endTime);
        this.name = name;
        this.locationName = locationName;
        this.member = member;
    }

    public static TourLog of(
        String name,
        String locationName,
        Member member,
        LocalDateTime startTime,
        LocalDateTime endTime
    ) {
        return new TourLog(name, locationName, member, startTime, endTime);
    }
}
