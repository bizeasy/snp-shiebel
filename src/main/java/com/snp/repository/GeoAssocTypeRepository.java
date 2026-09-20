package com.snp.repository;

import com.snp.domain.GeoAssocType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GeoAssocType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GeoAssocTypeRepository extends JpaRepository<GeoAssocType, Long>, JpaSpecificationExecutor<GeoAssocType> {}
