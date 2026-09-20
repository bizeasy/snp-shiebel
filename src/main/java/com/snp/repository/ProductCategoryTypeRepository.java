package com.snp.repository;

import com.snp.domain.ProductCategoryType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductCategoryType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductCategoryTypeRepository
    extends JpaRepository<ProductCategoryType, Long>, JpaSpecificationExecutor<ProductCategoryType> {}
