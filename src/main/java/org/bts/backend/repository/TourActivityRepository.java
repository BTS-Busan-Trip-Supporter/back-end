package org.bts.backend.repository;

import org.bts.backend.domain.TourActivity;
import org.bts.backend.domain.constant.DayTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TourActivityRepository extends JpaRepository<TourActivity, Long> {

    @Modifying
    @Query("update TourActivity ta set ta.orderIndex = ta.orderIndex + 1 "
        + "where ta.tourLog.id = :tourLogId and ta.dayNumber = :dayNumber and ta.dayTime = :dayTime and ta.orderIndex >= :startIndex")
    void incrementOrderIndexFrom(Long tourLogId, Integer dayNumber, DayTime dayTime, Integer startIndex);

    @Modifying
    @Query("update TourActivity ta set ta.orderIndex = ta.orderIndex - 1 "
        + "where ta.tourLog.id = :tourLogId and ta.dayNumber = :dayNumber and ta.dayTime = :dayTime and ta.orderIndex >= :startIndex")
    void decrementOrderIndexFrom(Long tourLogId, Integer dayNumber, DayTime dayTime, Integer startIndex);
}
