package com.snp.repository;

import com.snp.domain.ProductFeatureCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductFeatureCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductFeatureCategoryRepository
    extends JpaRepository<ProductFeatureCategory, Long>, JpaSpecificationExecutor<ProductFeatureCategory> {}
