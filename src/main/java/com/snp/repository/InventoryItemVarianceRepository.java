package com.snp.repository;

import com.snp.domain.InventoryItemVariance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InventoryItemVariance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InventoryItemVarianceRepository
    extends JpaRepository<InventoryItemVariance, Long>, JpaSpecificationExecutor<InventoryItemVariance> {}
