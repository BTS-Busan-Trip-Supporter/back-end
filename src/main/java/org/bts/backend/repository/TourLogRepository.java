package org.bts.backend.repository;

import org.bts.backend.domain.TourLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TourLogRepository extends JpaRepository<TourLog, Long> {

    @Query("select tl from TourLog tl join fetch tl.tourActivities as ta join fetch ta.tourSpot where tl.id = :id")
    TourLog findByIdWithTourActivities(Long id);
}
