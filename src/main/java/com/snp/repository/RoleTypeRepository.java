package com.snp.repository;

import com.snp.domain.RoleType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RoleType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoleTypeRepository extends JpaRepository<RoleType, Long>, JpaSpecificationExecutor<RoleType> {}
