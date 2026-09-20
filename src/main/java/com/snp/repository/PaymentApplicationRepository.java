package com.snp.repository;

import com.snp.domain.PaymentApplication;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PaymentApplication entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentApplicationRepository
    extends JpaRepository<PaymentApplication, Long>, JpaSpecificationExecutor<PaymentApplication> {}
