package com.snp.repository;

import com.snp.domain.SalesChannel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SalesChannel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalesChannelRepository extends JpaRepository<SalesChannel, Long>, JpaSpecificationExecutor<SalesChannel> {}
