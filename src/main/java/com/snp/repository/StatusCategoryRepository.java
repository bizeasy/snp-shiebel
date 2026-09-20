package com.snp.repository;

import com.snp.domain.StatusCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StatusCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StatusCategoryRepository extends JpaRepository<StatusCategory, Long>, JpaSpecificationExecutor<StatusCategory> {}
