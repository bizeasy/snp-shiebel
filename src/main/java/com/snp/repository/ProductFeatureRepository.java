package com.snp.repository;

import com.snp.domain.ProductFeature;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductFeature entity.
 */
@Repository
public interface ProductFeatureRepository extends JpaRepository<ProductFeature, Long>, JpaSpecificationExecutor<ProductFeature> {
    default Optional<ProductFeature> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProductFeature> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProductFeature> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select productFeature from ProductFeature productFeature left join fetch productFeature.productFeatureType left join fetch productFeature.productFeatureCategory",
        countQuery = "select count(productFeature) from ProductFeature productFeature"
    )
    Page<ProductFeature> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select productFeature from ProductFeature productFeature left join fetch productFeature.productFeatureType left join fetch productFeature.productFeatureCategory"
    )
    List<ProductFeature> findAllWithToOneRelationships();

    @Query(
        "select productFeature from ProductFeature productFeature left join fetch productFeature.productFeatureType left join fetch productFeature.productFeatureCategory where productFeature.id =:id"
    )
    Optional<ProductFeature> findOneWithToOneRelationships(@Param("id") Long id);
}
