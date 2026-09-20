package com.snp.repository;

import com.snp.domain.PartyType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PartyType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PartyTypeRepository extends JpaRepository<PartyType, Long>, JpaSpecificationExecutor<PartyType> {}
