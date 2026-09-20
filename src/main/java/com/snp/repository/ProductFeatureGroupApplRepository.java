package com.snp.repository;

import com.snp.domain.ProductFeatureGroupAppl;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductFeatureGroupAppl entity.
 */
@Repository
public interface ProductFeatureGroupApplRepository
    extends JpaRepository<ProductFeatureGroupAppl, Long>, JpaSpecificationExecutor<ProductFeatureGroupAppl>
{
    default Optional<ProductFeatureGroupAppl> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProductFeatureGroupAppl> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProductFeatureGroupAppl> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select productFeatureGroupAppl from ProductFeatureGroupAppl productFeatureGroupAppl left join fetch productFeatureGroupAppl.productFeatureGroup left join fetch productFeatureGroupAppl.productFeature",
        countQuery = "select count(productFeatureGroupAppl) from ProductFeatureGroupAppl productFeatureGroupAppl"
    )
    Page<ProductFeatureGroupAppl> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select productFeatureGroupAppl from ProductFeatureGroupAppl productFeatureGroupAppl left join fetch productFeatureGroupAppl.productFeatureGroup left join fetch productFeatureGroupAppl.productFeature"
    )
    List<ProductFeatureGroupAppl> findAllWithToOneRelationships();

    @Query(
        "select productFeatureGroupAppl from ProductFeatureGroupAppl productFeatureGroupAppl left join fetch productFeatureGroupAppl.productFeatureGroup left join fetch productFeatureGroupAppl.productFeature where productFeatureGroupAppl.id =:id"
    )
    Optional<ProductFeatureGroupAppl> findOneWithToOneRelationships(@Param("id") Long id);
}
