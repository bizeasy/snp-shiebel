package com.snp.repository;

import com.snp.domain.FacilityType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FacilityType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FacilityTypeRepository extends JpaRepository<FacilityType, Long>, JpaSpecificationExecutor<FacilityType> {}
