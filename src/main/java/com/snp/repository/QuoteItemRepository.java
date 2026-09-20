package com.snp.repository;

import com.snp.domain.QuoteItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the QuoteItem entity.
 */
@Repository
public interface QuoteItemRepository extends JpaRepository<QuoteItem, Long>, JpaSpecificationExecutor<QuoteItem> {
    default Optional<QuoteItem> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<QuoteItem> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<QuoteItem> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select quoteItem from QuoteItem quoteItem left join fetch quoteItem.product left join fetch quoteItem.productFeature left join fetch quoteItem.uom left join fetch quoteItem.status",
        countQuery = "select count(quoteItem) from QuoteItem quoteItem"
    )
    Page<QuoteItem> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select quoteItem from QuoteItem quoteItem left join fetch quoteItem.product left join fetch quoteItem.productFeature left join fetch quoteItem.uom left join fetch quoteItem.status"
    )
    List<QuoteItem> findAllWithToOneRelationships();

    @Query(
        "select quoteItem from QuoteItem quoteItem left join fetch quoteItem.product left join fetch quoteItem.productFeature left join fetch quoteItem.uom left join fetch quoteItem.status where quoteItem.id =:id"
    )
    Optional<QuoteItem> findOneWithToOneRelationships(@Param("id") Long id);
}
