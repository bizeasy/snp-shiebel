package com.snp.repository;

import com.snp.domain.InvoiceType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InvoiceType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceTypeRepository extends JpaRepository<InvoiceType, Long>, JpaSpecificationExecutor<InvoiceType> {}
