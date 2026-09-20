package com.snp.repository;

import com.snp.domain.ProductFeatureApplType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductFeatureApplType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductFeatureApplTypeRepository
    extends JpaRepository<ProductFeatureApplType, Long>, JpaSpecificationExecutor<ProductFeatureApplType> {}
