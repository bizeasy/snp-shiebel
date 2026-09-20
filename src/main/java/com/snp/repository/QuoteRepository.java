package com.snp.repository;

import com.snp.domain.Quote;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Quote entity.
 */
@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long>, JpaSpecificationExecutor<Quote> {
    default Optional<Quote> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Quote> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Quote> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select quote from Quote quote left join fetch quote.quoteType left join fetch quote.custRequest left join fetch quote.status left join fetch quote.productStore left join fetch quote.salesChannel left join fetch quote.currencyUom left join fetch quote.currencyConversion",
        countQuery = "select count(quote) from Quote quote"
    )
    Page<Quote> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select quote from Quote quote left join fetch quote.quoteType left join fetch quote.custRequest left join fetch quote.status left join fetch quote.productStore left join fetch quote.salesChannel left join fetch quote.currencyUom left join fetch quote.currencyConversion"
    )
    List<Quote> findAllWithToOneRelationships();

    @Query(
        "select quote from Quote quote left join fetch quote.quoteType left join fetch quote.custRequest left join fetch quote.status left join fetch quote.productStore left join fetch quote.salesChannel left join fetch quote.currencyUom left join fetch quote.currencyConversion where quote.id =:id"
    )
    Optional<Quote> findOneWithToOneRelationships(@Param("id") Long id);
}
