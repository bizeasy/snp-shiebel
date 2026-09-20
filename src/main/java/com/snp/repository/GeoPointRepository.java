package com.snp.repository;

import com.snp.domain.GeoPoint;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GeoPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GeoPointRepository extends JpaRepository<GeoPoint, Long>, JpaSpecificationExecutor<GeoPoint> {}
