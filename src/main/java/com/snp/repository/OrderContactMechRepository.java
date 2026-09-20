package com.snp.repository;

import com.snp.domain.OrderContactMech;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrderContactMech entity.
 */
@Repository
public interface OrderContactMechRepository extends JpaRepository<OrderContactMech, Long>, JpaSpecificationExecutor<OrderContactMech> {
    default Optional<OrderContactMech> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<OrderContactMech> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<OrderContactMech> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select orderContactMech from OrderContactMech orderContactMech left join fetch orderContactMech.contactMechPurpose",
        countQuery = "select count(orderContactMech) from OrderContactMech orderContactMech"
    )
    Page<OrderContactMech> findAllWithToOneRelationships(Pageable pageable);

    @Query("select orderContactMech from OrderContactMech orderContactMech left join fetch orderContactMech.contactMechPurpose")
    List<OrderContactMech> findAllWithToOneRelationships();

    @Query(
        "select orderContactMech from OrderContactMech orderContactMech left join fetch orderContactMech.contactMechPurpose where orderContactMech.id =:id"
    )
    Optional<OrderContactMech> findOneWithToOneRelationships(@Param("id") Long id);
}
