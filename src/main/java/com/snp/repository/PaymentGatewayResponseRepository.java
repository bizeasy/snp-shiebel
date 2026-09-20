package com.snp.repository;

import com.snp.domain.PaymentGatewayResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PaymentGatewayResponse entity.
 */
@Repository
public interface PaymentGatewayResponseRepository
    extends JpaRepository<PaymentGatewayResponse, Long>, JpaSpecificationExecutor<PaymentGatewayResponse>
{
    default Optional<PaymentGatewayResponse> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PaymentGatewayResponse> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PaymentGatewayResponse> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select paymentGatewayResponse from PaymentGatewayResponse paymentGatewayResponse left join fetch paymentGatewayResponse.paymentGatewayConfig left join fetch paymentGatewayResponse.paymentMethodType",
        countQuery = "select count(paymentGatewayResponse) from PaymentGatewayResponse paymentGatewayResponse"
    )
    Page<PaymentGatewayResponse> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select paymentGatewayResponse from PaymentGatewayResponse paymentGatewayResponse left join fetch paymentGatewayResponse.paymentGatewayConfig left join fetch paymentGatewayResponse.paymentMethodType"
    )
    List<PaymentGatewayResponse> findAllWithToOneRelationships();

    @Query(
        "select paymentGatewayResponse from PaymentGatewayResponse paymentGatewayResponse left join fetch paymentGatewayResponse.paymentGatewayConfig left join fetch paymentGatewayResponse.paymentMethodType where paymentGatewayResponse.id =:id"
    )
    Optional<PaymentGatewayResponse> findOneWithToOneRelationships(@Param("id") Long id);
}
