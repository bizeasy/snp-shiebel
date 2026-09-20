package com.snp.repository;

import com.snp.domain.CustomTimePeriod;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CustomTimePeriod entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomTimePeriodRepository extends JpaRepository<CustomTimePeriod, Long>, JpaSpecificationExecutor<CustomTimePeriod> {}
