package com.snp.repository;

import com.snp.domain.OrderType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrderType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderTypeRepository extends JpaRepository<OrderType, Long>, JpaSpecificationExecutor<OrderType> {}
