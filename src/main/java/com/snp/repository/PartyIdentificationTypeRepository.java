package com.snp.repository;

import com.snp.domain.PartyIdentificationType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PartyIdentificationType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PartyIdentificationTypeRepository
    extends JpaRepository<PartyIdentificationType, Long>, JpaSpecificationExecutor<PartyIdentificationType> {}
