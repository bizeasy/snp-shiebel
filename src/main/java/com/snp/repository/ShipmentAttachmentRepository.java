package com.snp.repository;

import com.snp.domain.ShipmentAttachment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ShipmentAttachment entity.
 */
@Repository
public interface ShipmentAttachmentRepository
    extends JpaRepository<ShipmentAttachment, Long>, JpaSpecificationExecutor<ShipmentAttachment>
{
    default Optional<ShipmentAttachment> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ShipmentAttachment> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ShipmentAttachment> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select shipmentAttachment from ShipmentAttachment shipmentAttachment left join fetch shipmentAttachment.attachment",
        countQuery = "select count(shipmentAttachment) from ShipmentAttachment shipmentAttachment"
    )
    Page<ShipmentAttachment> findAllWithToOneRelationships(Pageable pageable);

    @Query("select shipmentAttachment from ShipmentAttachment shipmentAttachment left join fetch shipmentAttachment.attachment")
    List<ShipmentAttachment> findAllWithToOneRelationships();

    @Query(
        "select shipmentAttachment from ShipmentAttachment shipmentAttachment left join fetch shipmentAttachment.attachment where shipmentAttachment.id =:id"
    )
    Optional<ShipmentAttachment> findOneWithToOneRelationships(@Param("id") Long id);
}
