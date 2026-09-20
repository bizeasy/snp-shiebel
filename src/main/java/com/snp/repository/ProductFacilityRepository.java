package com.snp.repository;

import com.snp.domain.ProductFacility;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductFacility entity.
 */
@Repository
public interface ProductFacilityRepository extends JpaRepository<ProductFacility, Long>, JpaSpecificationExecutor<ProductFacility> {
    default Optional<ProductFacility> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProductFacility> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProductFacility> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select productFacility from ProductFacility productFacility left join fetch productFacility.product left join fetch productFacility.facility",
        countQuery = "select count(productFacility) from ProductFacility productFacility"
    )
    Page<ProductFacility> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select productFacility from ProductFacility productFacility left join fetch productFacility.product left join fetch productFacility.facility"
    )
    List<ProductFacility> findAllWithToOneRelationships();

    @Query(
        "select productFacility from ProductFacility productFacility left join fetch productFacility.product left join fetch productFacility.facility where productFacility.id =:id"
    )
    Optional<ProductFacility> findOneWithToOneRelationships(@Param("id") Long id);
}
