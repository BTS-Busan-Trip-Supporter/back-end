package org.bts.backend.repository;

import org.bts.backend.domain.TourLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourLogRepository extends JpaRepository<TourLog, String> {

}
