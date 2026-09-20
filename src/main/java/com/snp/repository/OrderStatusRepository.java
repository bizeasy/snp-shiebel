package com.snp.repository;

import com.snp.domain.OrderStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrderStatus entity.
 */
@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long>, JpaSpecificationExecutor<OrderStatus> {
    default Optional<OrderStatus> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<OrderStatus> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<OrderStatus> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select orderStatus from OrderStatus orderStatus left join fetch orderStatus.status",
        countQuery = "select count(orderStatus) from OrderStatus orderStatus"
    )
    Page<OrderStatus> findAllWithToOneRelationships(Pageable pageable);

    @Query("select orderStatus from OrderStatus orderStatus left join fetch orderStatus.status")
    List<OrderStatus> findAllWithToOneRelationships();

    @Query("select orderStatus from OrderStatus orderStatus left join fetch orderStatus.status where orderStatus.id =:id")
    Optional<OrderStatus> findOneWithToOneRelationships(@Param("id") Long id);
}
