package com.snp.repository;

import com.snp.domain.FacilityGroupType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FacilityGroupType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FacilityGroupTypeRepository extends JpaRepository<FacilityGroupType, Long>, JpaSpecificationExecutor<FacilityGroupType> {}
