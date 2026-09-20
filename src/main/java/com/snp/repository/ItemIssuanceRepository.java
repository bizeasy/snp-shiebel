package com.snp.repository;

import com.snp.domain.ItemIssuance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ItemIssuance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemIssuanceRepository extends JpaRepository<ItemIssuance, Long>, JpaSpecificationExecutor<ItemIssuance> {}
