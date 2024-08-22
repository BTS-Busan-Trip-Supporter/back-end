package org.bts.backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TourSpotRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double averageRating;

    @OneToOne
    @MapsId
    @JoinColumn(name = "content_id")
    private TourSpot tourSpot;
}
