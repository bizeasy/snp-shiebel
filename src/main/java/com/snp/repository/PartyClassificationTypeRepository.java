package com.snp.repository;

import com.snp.domain.PartyClassificationType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PartyClassificationType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PartyClassificationTypeRepository
    extends JpaRepository<PartyClassificationType, Long>, JpaSpecificationExecutor<PartyClassificationType> {}
