package com.snp.repository;

import com.snp.domain.InventoryItemDelegate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InventoryItemDelegate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InventoryItemDelegateRepository
    extends JpaRepository<InventoryItemDelegate, Long>, JpaSpecificationExecutor<InventoryItemDelegate> {}
