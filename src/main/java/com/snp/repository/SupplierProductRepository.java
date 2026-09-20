package com.snp.repository;

import com.snp.domain.SupplierProduct;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SupplierProduct entity.
 */
@Repository
public interface SupplierProductRepository extends JpaRepository<SupplierProduct, Long>, JpaSpecificationExecutor<SupplierProduct> {
    default Optional<SupplierProduct> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SupplierProduct> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SupplierProduct> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select supplierProduct from SupplierProduct supplierProduct left join fetch supplierProduct.product left join fetch supplierProduct.quantityUom left join fetch supplierProduct.currencyUom",
        countQuery = "select count(supplierProduct) from SupplierProduct supplierProduct"
    )
    Page<SupplierProduct> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select supplierProduct from SupplierProduct supplierProduct left join fetch supplierProduct.product left join fetch supplierProduct.quantityUom left join fetch supplierProduct.currencyUom"
    )
    List<SupplierProduct> findAllWithToOneRelationships();

    @Query(
        "select supplierProduct from SupplierProduct supplierProduct left join fetch supplierProduct.product left join fetch supplierProduct.quantityUom left join fetch supplierProduct.currencyUom where supplierProduct.id =:id"
    )
    Optional<SupplierProduct> findOneWithToOneRelationships(@Param("id") Long id);
}
