package com.snp.repository;

import com.snp.domain.ProductStore;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductStore entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductStoreRepository extends JpaRepository<ProductStore, Long>, JpaSpecificationExecutor<ProductStore> {}
