package com.snp.repository;

import com.snp.domain.InventoryTransfer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InventoryTransfer entity.
 */
@Repository
public interface InventoryTransferRepository extends JpaRepository<InventoryTransfer, Long>, JpaSpecificationExecutor<InventoryTransfer> {
    default Optional<InventoryTransfer> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<InventoryTransfer> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<InventoryTransfer> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select inventoryTransfer from InventoryTransfer inventoryTransfer left join fetch inventoryTransfer.status left join fetch inventoryTransfer.facility left join fetch inventoryTransfer.facilityLocation left join fetch inventoryTransfer.facilityTo left join fetch inventoryTransfer.facilityLocationTo",
        countQuery = "select count(inventoryTransfer) from InventoryTransfer inventoryTransfer"
    )
    Page<InventoryTransfer> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select inventoryTransfer from InventoryTransfer inventoryTransfer left join fetch inventoryTransfer.status left join fetch inventoryTransfer.facility left join fetch inventoryTransfer.facilityLocation left join fetch inventoryTransfer.facilityTo left join fetch inventoryTransfer.facilityLocationTo"
    )
    List<InventoryTransfer> findAllWithToOneRelationships();

    @Query(
        "select inventoryTransfer from InventoryTransfer inventoryTransfer left join fetch inventoryTransfer.status left join fetch inventoryTransfer.facility left join fetch inventoryTransfer.facilityLocation left join fetch inventoryTransfer.facilityTo left join fetch inventoryTransfer.facilityLocationTo where inventoryTransfer.id =:id"
    )
    Optional<InventoryTransfer> findOneWithToOneRelationships(@Param("id") Long id);
}
