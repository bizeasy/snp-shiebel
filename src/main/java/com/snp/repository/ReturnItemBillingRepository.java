package com.snp.repository;

import com.snp.domain.ReturnItemBilling;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReturnItemBilling entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReturnItemBillingRepository extends JpaRepository<ReturnItemBilling, Long>, JpaSpecificationExecutor<ReturnItemBilling> {}
