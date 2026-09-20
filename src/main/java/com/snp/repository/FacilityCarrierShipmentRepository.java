package com.snp.repository;

import com.snp.domain.FacilityCarrierShipment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FacilityCarrierShipment entity.
 */
@Repository
public interface FacilityCarrierShipmentRepository
    extends JpaRepository<FacilityCarrierShipment, Long>, JpaSpecificationExecutor<FacilityCarrierShipment>
{
    default Optional<FacilityCarrierShipment> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<FacilityCarrierShipment> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<FacilityCarrierShipment> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select facilityCarrierShipment from FacilityCarrierShipment facilityCarrierShipment left join fetch facilityCarrierShipment.facility left join fetch facilityCarrierShipment.roleType left join fetch facilityCarrierShipment.shipmentMethodType",
        countQuery = "select count(facilityCarrierShipment) from FacilityCarrierShipment facilityCarrierShipment"
    )
    Page<FacilityCarrierShipment> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select facilityCarrierShipment from FacilityCarrierShipment facilityCarrierShipment left join fetch facilityCarrierShipment.facility left join fetch facilityCarrierShipment.roleType left join fetch facilityCarrierShipment.shipmentMethodType"
    )
    List<FacilityCarrierShipment> findAllWithToOneRelationships();

    @Query(
        "select facilityCarrierShipment from FacilityCarrierShipment facilityCarrierShipment left join fetch facilityCarrierShipment.facility left join fetch facilityCarrierShipment.roleType left join fetch facilityCarrierShipment.shipmentMethodType where facilityCarrierShipment.id =:id"
    )
    Optional<FacilityCarrierShipment> findOneWithToOneRelationships(@Param("id") Long id);
}
