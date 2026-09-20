package com.snp.repository;

import com.snp.domain.CarrierShipmentMethod;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CarrierShipmentMethod entity.
 */
@Repository
public interface CarrierShipmentMethodRepository
    extends JpaRepository<CarrierShipmentMethod, Long>, JpaSpecificationExecutor<CarrierShipmentMethod>
{
    default Optional<CarrierShipmentMethod> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CarrierShipmentMethod> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CarrierShipmentMethod> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select carrierShipmentMethod from CarrierShipmentMethod carrierShipmentMethod left join fetch carrierShipmentMethod.shipmentMethodType left join fetch carrierShipmentMethod.roleType",
        countQuery = "select count(carrierShipmentMethod) from CarrierShipmentMethod carrierShipmentMethod"
    )
    Page<CarrierShipmentMethod> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select carrierShipmentMethod from CarrierShipmentMethod carrierShipmentMethod left join fetch carrierShipmentMethod.shipmentMethodType left join fetch carrierShipmentMethod.roleType"
    )
    List<CarrierShipmentMethod> findAllWithToOneRelationships();

    @Query(
        "select carrierShipmentMethod from CarrierShipmentMethod carrierShipmentMethod left join fetch carrierShipmentMethod.shipmentMethodType left join fetch carrierShipmentMethod.roleType where carrierShipmentMethod.id =:id"
    )
    Optional<CarrierShipmentMethod> findOneWithToOneRelationships(@Param("id") Long id);
}
