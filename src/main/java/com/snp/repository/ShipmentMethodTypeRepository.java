package com.snp.repository;

import com.snp.domain.ShipmentMethodType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ShipmentMethodType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentMethodTypeRepository
    extends JpaRepository<ShipmentMethodType, Long>, JpaSpecificationExecutor<ShipmentMethodType> {}
