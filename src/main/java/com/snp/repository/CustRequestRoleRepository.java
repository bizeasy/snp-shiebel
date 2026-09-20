package com.snp.repository;

import com.snp.domain.CustRequestRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CustRequestRole entity.
 */
@Repository
public interface CustRequestRoleRepository extends JpaRepository<CustRequestRole, Long>, JpaSpecificationExecutor<CustRequestRole> {
    default Optional<CustRequestRole> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CustRequestRole> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CustRequestRole> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select custRequestRole from CustRequestRole custRequestRole left join fetch custRequestRole.roleType",
        countQuery = "select count(custRequestRole) from CustRequestRole custRequestRole"
    )
    Page<CustRequestRole> findAllWithToOneRelationships(Pageable pageable);

    @Query("select custRequestRole from CustRequestRole custRequestRole left join fetch custRequestRole.roleType")
    List<CustRequestRole> findAllWithToOneRelationships();

    @Query(
        "select custRequestRole from CustRequestRole custRequestRole left join fetch custRequestRole.roleType where custRequestRole.id =:id"
    )
    Optional<CustRequestRole> findOneWithToOneRelationships(@Param("id") Long id);
}
