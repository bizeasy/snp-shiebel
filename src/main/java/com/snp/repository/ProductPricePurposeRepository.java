package com.snp.repository;

import com.snp.domain.ProductPricePurpose;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductPricePurpose entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductPricePurposeRepository
    extends JpaRepository<ProductPricePurpose, Long>, JpaSpecificationExecutor<ProductPricePurpose> {}
