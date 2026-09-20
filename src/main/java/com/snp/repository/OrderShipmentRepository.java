package com.snp.repository;

import com.snp.domain.OrderShipment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrderShipment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderShipmentRepository extends JpaRepository<OrderShipment, Long>, JpaSpecificationExecutor<OrderShipment> {}
