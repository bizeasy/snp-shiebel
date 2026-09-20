package com.snp.repository;

import com.snp.domain.OrderItemBilling;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrderItemBilling entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderItemBillingRepository extends JpaRepository<OrderItemBilling, Long>, JpaSpecificationExecutor<OrderItemBilling> {}
