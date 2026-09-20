package com.snp.repository;

import com.snp.domain.ProductPriceType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductPriceType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductPriceTypeRepository extends JpaRepository<ProductPriceType, Long>, JpaSpecificationExecutor<ProductPriceType> {}
