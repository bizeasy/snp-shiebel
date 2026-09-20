package com.snp.repository;

import com.snp.domain.InventoryItemType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InventoryItemType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InventoryItemTypeRepository extends JpaRepository<InventoryItemType, Long>, JpaSpecificationExecutor<InventoryItemType> {}
