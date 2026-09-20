package com.snp.repository;

import com.snp.domain.Facility;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Facility entity.
 */
@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long>, JpaSpecificationExecutor<Facility> {
    default Optional<Facility> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Facility> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Facility> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select facility from Facility facility left join fetch facility.facilityType left join fetch facility.parentFacility left join fetch facility.facilityGroup left join fetch facility.sizeUom left join fetch facility.inventoryItemType left join fetch facility.productStore",
        countQuery = "select count(facility) from Facility facility"
    )
    Page<Facility> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select facility from Facility facility left join fetch facility.facilityType left join fetch facility.parentFacility left join fetch facility.facilityGroup left join fetch facility.sizeUom left join fetch facility.inventoryItemType left join fetch facility.productStore"
    )
    List<Facility> findAllWithToOneRelationships();

    @Query(
        "select facility from Facility facility left join fetch facility.facilityType left join fetch facility.parentFacility left join fetch facility.facilityGroup left join fetch facility.sizeUom left join fetch facility.inventoryItemType left join fetch facility.productStore where facility.id =:id"
    )
    Optional<Facility> findOneWithToOneRelationships(@Param("id") Long id);
}
