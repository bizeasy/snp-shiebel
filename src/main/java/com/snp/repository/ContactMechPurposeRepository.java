package com.snp.repository;

import com.snp.domain.ContactMechPurpose;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ContactMechPurpose entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactMechPurposeRepository
    extends JpaRepository<ContactMechPurpose, Long>, JpaSpecificationExecutor<ContactMechPurpose> {}
