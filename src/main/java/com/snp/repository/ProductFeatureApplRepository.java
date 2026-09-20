package com.snp.repository;

import com.snp.domain.ProductFeatureAppl;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductFeatureAppl entity.
 */
@Repository
public interface ProductFeatureApplRepository
    extends JpaRepository<ProductFeatureAppl, Long>, JpaSpecificationExecutor<ProductFeatureAppl>
{
    default Optional<ProductFeatureAppl> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProductFeatureAppl> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProductFeatureAppl> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select productFeatureAppl from ProductFeatureAppl productFeatureAppl left join fetch productFeatureAppl.product left join fetch productFeatureAppl.productFeature left join fetch productFeatureAppl.featureApplType",
        countQuery = "select count(productFeatureAppl) from ProductFeatureAppl productFeatureAppl"
    )
    Page<ProductFeatureAppl> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select productFeatureAppl from ProductFeatureAppl productFeatureAppl left join fetch productFeatureAppl.product left join fetch productFeatureAppl.productFeature left join fetch productFeatureAppl.featureApplType"
    )
    List<ProductFeatureAppl> findAllWithToOneRelationships();

    @Query(
        "select productFeatureAppl from ProductFeatureAppl productFeatureAppl left join fetch productFeatureAppl.product left join fetch productFeatureAppl.productFeature left join fetch productFeatureAppl.featureApplType where productFeatureAppl.id =:id"
    )
    Optional<ProductFeatureAppl> findOneWithToOneRelationships(@Param("id") Long id);
}
