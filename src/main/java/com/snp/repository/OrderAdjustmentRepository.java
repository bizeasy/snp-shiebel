package com.snp.repository;

import com.snp.domain.OrderAdjustment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrderAdjustment entity.
 */
@Repository
public interface OrderAdjustmentRepository extends JpaRepository<OrderAdjustment, Long>, JpaSpecificationExecutor<OrderAdjustment> {
    default Optional<OrderAdjustment> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<OrderAdjustment> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<OrderAdjustment> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select orderAdjustment from OrderAdjustment orderAdjustment left join fetch orderAdjustment.adjustmentType left join fetch orderAdjustment.product left join fetch orderAdjustment.productFeature left join fetch orderAdjustment.taxAuthority",
        countQuery = "select count(orderAdjustment) from OrderAdjustment orderAdjustment"
    )
    Page<OrderAdjustment> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select orderAdjustment from OrderAdjustment orderAdjustment left join fetch orderAdjustment.adjustmentType left join fetch orderAdjustment.product left join fetch orderAdjustment.productFeature left join fetch orderAdjustment.taxAuthority"
    )
    List<OrderAdjustment> findAllWithToOneRelationships();

    @Query(
        "select orderAdjustment from OrderAdjustment orderAdjustment left join fetch orderAdjustment.adjustmentType left join fetch orderAdjustment.product left join fetch orderAdjustment.productFeature left join fetch orderAdjustment.taxAuthority where orderAdjustment.id =:id"
    )
    Optional<OrderAdjustment> findOneWithToOneRelationships(@Param("id") Long id);
}
