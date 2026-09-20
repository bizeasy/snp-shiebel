package com.snp.repository;

import com.snp.domain.ContactMechType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ContactMechType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactMechTypeRepository extends JpaRepository<ContactMechType, Long>, JpaSpecificationExecutor<ContactMechType> {}
