package com.snp.repository;

import com.snp.domain.PaymentGatewayConfig;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PaymentGatewayConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentGatewayConfigRepository
    extends JpaRepository<PaymentGatewayConfig, Long>, JpaSpecificationExecutor<PaymentGatewayConfig> {}
