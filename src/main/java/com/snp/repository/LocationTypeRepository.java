package com.snp.repository;

import com.snp.domain.LocationType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LocationType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationTypeRepository extends JpaRepository<LocationType, Long>, JpaSpecificationExecutor<LocationType> {}
