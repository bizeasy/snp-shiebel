package com.snp.repository;

import com.snp.domain.ShipmentReceipt;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ShipmentReceipt entity.
 */
@Repository
public interface ShipmentReceiptRepository extends JpaRepository<ShipmentReceipt, Long>, JpaSpecificationExecutor<ShipmentReceipt> {
    default Optional<ShipmentReceipt> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ShipmentReceipt> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ShipmentReceipt> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select shipmentReceipt from ShipmentReceipt shipmentReceipt left join fetch shipmentReceipt.product",
        countQuery = "select count(shipmentReceipt) from ShipmentReceipt shipmentReceipt"
    )
    Page<ShipmentReceipt> findAllWithToOneRelationships(Pageable pageable);

    @Query("select shipmentReceipt from ShipmentReceipt shipmentReceipt left join fetch shipmentReceipt.product")
    List<ShipmentReceipt> findAllWithToOneRelationships();

    @Query(
        "select shipmentReceipt from ShipmentReceipt shipmentReceipt left join fetch shipmentReceipt.product where shipmentReceipt.id =:id"
    )
    Optional<ShipmentReceipt> findOneWithToOneRelationships(@Param("id") Long id);
}
