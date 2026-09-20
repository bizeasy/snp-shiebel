package com.snp.repository;

import com.snp.domain.ShipmentType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ShipmentType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentTypeRepository extends JpaRepository<ShipmentType, Long>, JpaSpecificationExecutor<ShipmentType> {}
