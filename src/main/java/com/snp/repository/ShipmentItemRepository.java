package com.snp.repository;

import com.snp.domain.ShipmentItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ShipmentItem entity.
 */
@Repository
public interface ShipmentItemRepository extends JpaRepository<ShipmentItem, Long>, JpaSpecificationExecutor<ShipmentItem> {
    default Optional<ShipmentItem> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ShipmentItem> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ShipmentItem> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select shipmentItem from ShipmentItem shipmentItem left join fetch shipmentItem.product",
        countQuery = "select count(shipmentItem) from ShipmentItem shipmentItem"
    )
    Page<ShipmentItem> findAllWithToOneRelationships(Pageable pageable);

    @Query("select shipmentItem from ShipmentItem shipmentItem left join fetch shipmentItem.product")
    List<ShipmentItem> findAllWithToOneRelationships();

    @Query("select shipmentItem from ShipmentItem shipmentItem left join fetch shipmentItem.product where shipmentItem.id =:id")
    Optional<ShipmentItem> findOneWithToOneRelationships(@Param("id") Long id);
}
