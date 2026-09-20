package com.snp.repository;

import com.snp.domain.ProductAssocType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductAssocType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductAssocTypeRepository extends JpaRepository<ProductAssocType, Long>, JpaSpecificationExecutor<ProductAssocType> {}
