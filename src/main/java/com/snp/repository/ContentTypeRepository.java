package com.snp.repository;

import com.snp.domain.ContentType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ContentType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContentTypeRepository extends JpaRepository<ContentType, Long>, JpaSpecificationExecutor<ContentType> {}
