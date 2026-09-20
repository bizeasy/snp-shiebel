package com.snp.repository;

import com.snp.domain.UomType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UomType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UomTypeRepository extends JpaRepository<UomType, Long>, JpaSpecificationExecutor<UomType> {}
