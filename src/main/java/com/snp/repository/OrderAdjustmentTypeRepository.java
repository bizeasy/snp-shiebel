package com.snp.repository;

import com.snp.domain.OrderAdjustmentType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrderAdjustmentType entity.
 */
@Repository
public interface OrderAdjustmentTypeRepository
    extends JpaRepository<OrderAdjustmentType, Long>, JpaSpecificationExecutor<OrderAdjustmentType>
{
    default Optional<OrderAdjustmentType> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<OrderAdjustmentType> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<OrderAdjustmentType> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select orderAdjustmentType from OrderAdjustmentType orderAdjustmentType left join fetch orderAdjustmentType.parent",
        countQuery = "select count(orderAdjustmentType) from OrderAdjustmentType orderAdjustmentType"
    )
    Page<OrderAdjustmentType> findAllWithToOneRelationships(Pageable pageable);

    @Query("select orderAdjustmentType from OrderAdjustmentType orderAdjustmentType left join fetch orderAdjustmentType.parent")
    List<OrderAdjustmentType> findAllWithToOneRelationships();

    @Query(
        "select orderAdjustmentType from OrderAdjustmentType orderAdjustmentType left join fetch orderAdjustmentType.parent where orderAdjustmentType.id =:id"
    )
    Optional<OrderAdjustmentType> findOneWithToOneRelationships(@Param("id") Long id);
}
