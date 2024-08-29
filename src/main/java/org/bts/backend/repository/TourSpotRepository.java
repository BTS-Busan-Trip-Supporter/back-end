package org.bts.backend.repository;

import org.bts.backend.domain.TourSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourSpotRepository extends JpaRepository<TourSpot, String> {

}
