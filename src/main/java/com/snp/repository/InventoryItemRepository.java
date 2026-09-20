package com.snp.repository;

import com.snp.domain.InventoryItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InventoryItem entity.
 */
@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long>, JpaSpecificationExecutor<InventoryItem> {
    default Optional<InventoryItem> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<InventoryItem> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<InventoryItem> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select inventoryItem from InventoryItem inventoryItem left join fetch inventoryItem.inventoryItemType left join fetch inventoryItem.product left join fetch inventoryItem.supplier left join fetch inventoryItem.status left join fetch inventoryItem.facility left join fetch inventoryItem.facilityLocation left join fetch inventoryItem.uom left join fetch inventoryItem.currencyUom",
        countQuery = "select count(inventoryItem) from InventoryItem inventoryItem"
    )
    Page<InventoryItem> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select inventoryItem from InventoryItem inventoryItem left join fetch inventoryItem.inventoryItemType left join fetch inventoryItem.product left join fetch inventoryItem.supplier left join fetch inventoryItem.status left join fetch inventoryItem.facility left join fetch inventoryItem.facilityLocation left join fetch inventoryItem.uom left join fetch inventoryItem.currencyUom"
    )
    List<InventoryItem> findAllWithToOneRelationships();

    @Query(
        "select inventoryItem from InventoryItem inventoryItem left join fetch inventoryItem.inventoryItemType left join fetch inventoryItem.product left join fetch inventoryItem.supplier left join fetch inventoryItem.status left join fetch inventoryItem.facility left join fetch inventoryItem.facilityLocation left join fetch inventoryItem.uom left join fetch inventoryItem.currencyUom where inventoryItem.id =:id"
    )
    Optional<InventoryItem> findOneWithToOneRelationships(@Param("id") Long id);
}
