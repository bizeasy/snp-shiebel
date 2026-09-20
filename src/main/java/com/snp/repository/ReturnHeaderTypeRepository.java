package com.snp.repository;

import com.snp.domain.ReturnHeaderType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReturnHeaderType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReturnHeaderTypeRepository extends JpaRepository<ReturnHeaderType, Long>, JpaSpecificationExecutor<ReturnHeaderType> {}
