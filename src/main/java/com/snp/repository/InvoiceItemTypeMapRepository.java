package com.snp.repository;

import com.snp.domain.InvoiceItemTypeMap;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InvoiceItemTypeMap entity.
 */
@Repository
public interface InvoiceItemTypeMapRepository
    extends JpaRepository<InvoiceItemTypeMap, Long>, JpaSpecificationExecutor<InvoiceItemTypeMap>
{
    default Optional<InvoiceItemTypeMap> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<InvoiceItemTypeMap> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<InvoiceItemTypeMap> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select invoiceItemTypeMap from InvoiceItemTypeMap invoiceItemTypeMap left join fetch invoiceItemTypeMap.invoiceType left join fetch invoiceItemTypeMap.invoiceItemType",
        countQuery = "select count(invoiceItemTypeMap) from InvoiceItemTypeMap invoiceItemTypeMap"
    )
    Page<InvoiceItemTypeMap> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select invoiceItemTypeMap from InvoiceItemTypeMap invoiceItemTypeMap left join fetch invoiceItemTypeMap.invoiceType left join fetch invoiceItemTypeMap.invoiceItemType"
    )
    List<InvoiceItemTypeMap> findAllWithToOneRelationships();

    @Query(
        "select invoiceItemTypeMap from InvoiceItemTypeMap invoiceItemTypeMap left join fetch invoiceItemTypeMap.invoiceType left join fetch invoiceItemTypeMap.invoiceItemType where invoiceItemTypeMap.id =:id"
    )
    Optional<InvoiceItemTypeMap> findOneWithToOneRelationships(@Param("id") Long id);
}
