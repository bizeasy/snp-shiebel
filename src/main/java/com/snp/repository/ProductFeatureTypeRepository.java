package com.snp.repository;

import com.snp.domain.ProductFeatureType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductFeatureType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductFeatureTypeRepository
    extends JpaRepository<ProductFeatureType, Long>, JpaSpecificationExecutor<ProductFeatureType> {}
