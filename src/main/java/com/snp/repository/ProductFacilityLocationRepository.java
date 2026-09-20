package com.snp.repository;

import com.snp.domain.ProductFacilityLocation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductFacilityLocation entity.
 */
@Repository
public interface ProductFacilityLocationRepository
    extends JpaRepository<ProductFacilityLocation, Long>, JpaSpecificationExecutor<ProductFacilityLocation>
{
    default Optional<ProductFacilityLocation> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProductFacilityLocation> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProductFacilityLocation> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select productFacilityLocation from ProductFacilityLocation productFacilityLocation left join fetch productFacilityLocation.product left join fetch productFacilityLocation.facility left join fetch productFacilityLocation.facilityLocation",
        countQuery = "select count(productFacilityLocation) from ProductFacilityLocation productFacilityLocation"
    )
    Page<ProductFacilityLocation> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select productFacilityLocation from ProductFacilityLocation productFacilityLocation left join fetch productFacilityLocation.product left join fetch productFacilityLocation.facility left join fetch productFacilityLocation.facilityLocation"
    )
    List<ProductFacilityLocation> findAllWithToOneRelationships();

    @Query(
        "select productFacilityLocation from ProductFacilityLocation productFacilityLocation left join fetch productFacilityLocation.product left join fetch productFacilityLocation.facility left join fetch productFacilityLocation.facilityLocation where productFacilityLocation.id =:id"
    )
    Optional<ProductFacilityLocation> findOneWithToOneRelationships(@Param("id") Long id);
}
