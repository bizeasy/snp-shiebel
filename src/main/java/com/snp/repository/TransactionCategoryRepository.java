package com.snp.repository;

import com.snp.domain.TransactionCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TransactionCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionCategoryRepository
    extends JpaRepository<TransactionCategory, Long>, JpaSpecificationExecutor<TransactionCategory> {}
