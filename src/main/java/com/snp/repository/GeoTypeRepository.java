package com.snp.repository;

import com.snp.domain.GeoType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GeoType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GeoTypeRepository extends JpaRepository<GeoType, Long>, JpaSpecificationExecutor<GeoType> {}
