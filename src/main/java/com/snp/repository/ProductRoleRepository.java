package com.snp.repository;

import com.snp.domain.ProductRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductRole entity.
 */
@Repository
public interface ProductRoleRepository extends JpaRepository<ProductRole, Long>, JpaSpecificationExecutor<ProductRole> {
    default Optional<ProductRole> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProductRole> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProductRole> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select productRole from ProductRole productRole left join fetch productRole.product left join fetch productRole.roleType",
        countQuery = "select count(productRole) from ProductRole productRole"
    )
    Page<ProductRole> findAllWithToOneRelationships(Pageable pageable);

    @Query("select productRole from ProductRole productRole left join fetch productRole.product left join fetch productRole.roleType")
    List<ProductRole> findAllWithToOneRelationships();

    @Query(
        "select productRole from ProductRole productRole left join fetch productRole.product left join fetch productRole.roleType where productRole.id =:id"
    )
    Optional<ProductRole> findOneWithToOneRelationships(@Param("id") Long id);
}
