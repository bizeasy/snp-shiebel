package com.snp.repository;

import com.snp.domain.ProductPrice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductPrice entity.
 */
@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long>, JpaSpecificationExecutor<ProductPrice> {
    default Optional<ProductPrice> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProductPrice> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProductPrice> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select productPrice from ProductPrice productPrice left join fetch productPrice.product left join fetch productPrice.productPriceType left join fetch productPrice.productPricePurpose left join fetch productPrice.currencyUom",
        countQuery = "select count(productPrice) from ProductPrice productPrice"
    )
    Page<ProductPrice> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select productPrice from ProductPrice productPrice left join fetch productPrice.product left join fetch productPrice.productPriceType left join fetch productPrice.productPricePurpose left join fetch productPrice.currencyUom"
    )
    List<ProductPrice> findAllWithToOneRelationships();

    @Query(
        "select productPrice from ProductPrice productPrice left join fetch productPrice.product left join fetch productPrice.productPriceType left join fetch productPrice.productPricePurpose left join fetch productPrice.currencyUom where productPrice.id =:id"
    )
    Optional<ProductPrice> findOneWithToOneRelationships(@Param("id") Long id);
}
