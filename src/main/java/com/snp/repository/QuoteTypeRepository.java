package com.snp.repository;

import com.snp.domain.QuoteType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the QuoteType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuoteTypeRepository extends JpaRepository<QuoteType, Long>, JpaSpecificationExecutor<QuoteType> {}
