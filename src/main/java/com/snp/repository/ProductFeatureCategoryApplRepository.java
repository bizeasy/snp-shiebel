package com.snp.repository;

import com.snp.domain.ProductFeatureCategoryAppl;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductFeatureCategoryAppl entity.
 */
@Repository
public interface ProductFeatureCategoryApplRepository
    extends JpaRepository<ProductFeatureCategoryAppl, Long>, JpaSpecificationExecutor<ProductFeatureCategoryAppl>
{
    default Optional<ProductFeatureCategoryAppl> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProductFeatureCategoryAppl> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProductFeatureCategoryAppl> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select productFeatureCategoryAppl from ProductFeatureCategoryAppl productFeatureCategoryAppl left join fetch productFeatureCategoryAppl.productCategory left join fetch productFeatureCategoryAppl.productFeatureCategory",
        countQuery = "select count(productFeatureCategoryAppl) from ProductFeatureCategoryAppl productFeatureCategoryAppl"
    )
    Page<ProductFeatureCategoryAppl> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select productFeatureCategoryAppl from ProductFeatureCategoryAppl productFeatureCategoryAppl left join fetch productFeatureCategoryAppl.productCategory left join fetch productFeatureCategoryAppl.productFeatureCategory"
    )
    List<ProductFeatureCategoryAppl> findAllWithToOneRelationships();

    @Query(
        "select productFeatureCategoryAppl from ProductFeatureCategoryAppl productFeatureCategoryAppl left join fetch productFeatureCategoryAppl.productCategory left join fetch productFeatureCategoryAppl.productFeatureCategory where productFeatureCategoryAppl.id =:id"
    )
    Optional<ProductFeatureCategoryAppl> findOneWithToOneRelationships(@Param("id") Long id);
}
