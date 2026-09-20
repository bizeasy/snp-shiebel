package com.snp.repository;

import com.snp.domain.CatalogueCategoryType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CatalogueCategoryType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CatalogueCategoryTypeRepository
    extends JpaRepository<CatalogueCategoryType, Long>, JpaSpecificationExecutor<CatalogueCategoryType> {}
