package com.snp.repository;

import com.snp.domain.PaymentMethod;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PaymentMethod entity.
 */
@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long>, JpaSpecificationExecutor<PaymentMethod> {
    default Optional<PaymentMethod> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PaymentMethod> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PaymentMethod> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select paymentMethod from PaymentMethod paymentMethod left join fetch paymentMethod.paymentMethodType",
        countQuery = "select count(paymentMethod) from PaymentMethod paymentMethod"
    )
    Page<PaymentMethod> findAllWithToOneRelationships(Pageable pageable);

    @Query("select paymentMethod from PaymentMethod paymentMethod left join fetch paymentMethod.paymentMethodType")
    List<PaymentMethod> findAllWithToOneRelationships();

    @Query(
        "select paymentMethod from PaymentMethod paymentMethod left join fetch paymentMethod.paymentMethodType where paymentMethod.id =:id"
    )
    Optional<PaymentMethod> findOneWithToOneRelationships(@Param("id") Long id);
}
