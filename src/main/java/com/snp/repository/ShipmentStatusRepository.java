package com.snp.repository;

import com.snp.domain.ShipmentStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ShipmentStatus entity.
 */
@Repository
public interface ShipmentStatusRepository extends JpaRepository<ShipmentStatus, Long>, JpaSpecificationExecutor<ShipmentStatus> {
    default Optional<ShipmentStatus> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ShipmentStatus> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ShipmentStatus> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select shipmentStatus from ShipmentStatus shipmentStatus left join fetch shipmentStatus.status",
        countQuery = "select count(shipmentStatus) from ShipmentStatus shipmentStatus"
    )
    Page<ShipmentStatus> findAllWithToOneRelationships(Pageable pageable);

    @Query("select shipmentStatus from ShipmentStatus shipmentStatus left join fetch shipmentStatus.status")
    List<ShipmentStatus> findAllWithToOneRelationships();

    @Query("select shipmentStatus from ShipmentStatus shipmentStatus left join fetch shipmentStatus.status where shipmentStatus.id =:id")
    Optional<ShipmentStatus> findOneWithToOneRelationships(@Param("id") Long id);
}
