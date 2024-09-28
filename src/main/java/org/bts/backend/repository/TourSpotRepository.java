package org.bts.backend.repository;

import java.util.List;
import org.bts.backend.domain.TourSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourSpotRepository extends JpaRepository<TourSpot, String> {

    List<TourSpot> findAllByTypeIdAndSigunguCode(String contentTypeId, String sigunguCode);
}
