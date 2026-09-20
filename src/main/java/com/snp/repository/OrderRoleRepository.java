package com.snp.repository;

import com.snp.domain.OrderRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrderRole entity.
 */
@Repository
public interface OrderRoleRepository extends JpaRepository<OrderRole, Long>, JpaSpecificationExecutor<OrderRole> {
    default Optional<OrderRole> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<OrderRole> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<OrderRole> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select orderRole from OrderRole orderRole left join fetch orderRole.roleType",
        countQuery = "select count(orderRole) from OrderRole orderRole"
    )
    Page<OrderRole> findAllWithToOneRelationships(Pageable pageable);

    @Query("select orderRole from OrderRole orderRole left join fetch orderRole.roleType")
    List<OrderRole> findAllWithToOneRelationships();

    @Query("select orderRole from OrderRole orderRole left join fetch orderRole.roleType where orderRole.id =:id")
    Optional<OrderRole> findOneWithToOneRelationships(@Param("id") Long id);
}
