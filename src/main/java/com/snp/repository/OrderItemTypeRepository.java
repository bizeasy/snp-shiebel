package com.snp.repository;

import com.snp.domain.OrderItemType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrderItemType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderItemTypeRepository extends JpaRepository<OrderItemType, Long>, JpaSpecificationExecutor<OrderItemType> {}
