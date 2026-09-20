package com.snp.repository;

import com.snp.domain.InvoiceItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InvoiceItem entity.
 */
@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long>, JpaSpecificationExecutor<InvoiceItem> {
    default Optional<InvoiceItem> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<InvoiceItem> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<InvoiceItem> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select invoiceItem from InvoiceItem invoiceItem left join fetch invoiceItem.invoiceItemType left join fetch invoiceItem.product left join fetch invoiceItem.uom left join fetch invoiceItem.productFeature left join fetch invoiceItem.taxAuthRate",
        countQuery = "select count(invoiceItem) from InvoiceItem invoiceItem"
    )
    Page<InvoiceItem> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select invoiceItem from InvoiceItem invoiceItem left join fetch invoiceItem.invoiceItemType left join fetch invoiceItem.product left join fetch invoiceItem.uom left join fetch invoiceItem.productFeature left join fetch invoiceItem.taxAuthRate"
    )
    List<InvoiceItem> findAllWithToOneRelationships();

    @Query(
        "select invoiceItem from InvoiceItem invoiceItem left join fetch invoiceItem.invoiceItemType left join fetch invoiceItem.product left join fetch invoiceItem.uom left join fetch invoiceItem.productFeature left join fetch invoiceItem.taxAuthRate where invoiceItem.id =:id"
    )
    Optional<InvoiceItem> findOneWithToOneRelationships(@Param("id") Long id);
}
