package com.snp.repository;

import com.snp.domain.InvoiceItemType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InvoiceItemType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceItemTypeRepository extends JpaRepository<InvoiceItemType, Long>, JpaSpecificationExecutor<InvoiceItemType> {}
