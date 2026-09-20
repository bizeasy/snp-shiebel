package com.snp.repository;

import com.snp.domain.ProductFeatureGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductFeatureGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductFeatureGroupRepository
    extends JpaRepository<ProductFeatureGroup, Long>, JpaSpecificationExecutor<ProductFeatureGroup> {}
