package com.snp.repository;

import com.snp.domain.PaymentMethodType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PaymentMethodType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentMethodTypeRepository extends JpaRepository<PaymentMethodType, Long>, JpaSpecificationExecutor<PaymentMethodType> {}
