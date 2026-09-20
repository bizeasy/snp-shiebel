package com.snp.repository;

import com.snp.domain.OtpAuth;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OtpAuth entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OtpAuthRepository extends JpaRepository<OtpAuth, Long>, JpaSpecificationExecutor<OtpAuth> {}
