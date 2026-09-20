package com.snp.repository;

import com.snp.domain.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Order entity.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    default Optional<Order> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Order> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Order> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select order from Order order left join fetch order.orderType left join fetch order.salesChannel left join fetch order.status left join fetch order.currencyUom left join fetch order.currencyConversion left join fetch order.quote left join fetch order.facility left join fetch order.productStore left join fetch order.department",
        countQuery = "select count(order) from Order order"
    )
    Page<Order> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select order from Order order left join fetch order.orderType left join fetch order.salesChannel left join fetch order.status left join fetch order.currencyUom left join fetch order.currencyConversion left join fetch order.quote left join fetch order.facility left join fetch order.productStore left join fetch order.department"
    )
    List<Order> findAllWithToOneRelationships();

    @Query(
        "select order from Order order left join fetch order.orderType left join fetch order.salesChannel left join fetch order.status left join fetch order.currencyUom left join fetch order.currencyConversion left join fetch order.quote left join fetch order.facility left join fetch order.productStore left join fetch order.department where order.id =:id"
    )
    Optional<Order> findOneWithToOneRelationships(@Param("id") Long id);
}
