package com.snp.repository;

import com.snp.domain.QuoteAdjustment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the QuoteAdjustment entity.
 */
@Repository
public interface QuoteAdjustmentRepository extends JpaRepository<QuoteAdjustment, Long>, JpaSpecificationExecutor<QuoteAdjustment> {
    default Optional<QuoteAdjustment> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<QuoteAdjustment> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<QuoteAdjustment> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select quoteAdjustment from QuoteAdjustment quoteAdjustment left join fetch quoteAdjustment.adjustmentType left join fetch quoteAdjustment.product left join fetch quoteAdjustment.productFeature left join fetch quoteAdjustment.taxAuthority",
        countQuery = "select count(quoteAdjustment) from QuoteAdjustment quoteAdjustment"
    )
    Page<QuoteAdjustment> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select quoteAdjustment from QuoteAdjustment quoteAdjustment left join fetch quoteAdjustment.adjustmentType left join fetch quoteAdjustment.product left join fetch quoteAdjustment.productFeature left join fetch quoteAdjustment.taxAuthority"
    )
    List<QuoteAdjustment> findAllWithToOneRelationships();

    @Query(
        "select quoteAdjustment from QuoteAdjustment quoteAdjustment left join fetch quoteAdjustment.adjustmentType left join fetch quoteAdjustment.product left join fetch quoteAdjustment.productFeature left join fetch quoteAdjustment.taxAuthority where quoteAdjustment.id =:id"
    )
    Optional<QuoteAdjustment> findOneWithToOneRelationships(@Param("id") Long id);
}
