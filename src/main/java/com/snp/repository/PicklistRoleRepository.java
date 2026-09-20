package com.snp.repository;

import com.snp.domain.PicklistRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PicklistRole entity.
 */
@Repository
public interface PicklistRoleRepository extends JpaRepository<PicklistRole, Long>, JpaSpecificationExecutor<PicklistRole> {
    default Optional<PicklistRole> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PicklistRole> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PicklistRole> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select picklistRole from PicklistRole picklistRole left join fetch picklistRole.roleType",
        countQuery = "select count(picklistRole) from PicklistRole picklistRole"
    )
    Page<PicklistRole> findAllWithToOneRelationships(Pageable pageable);

    @Query("select picklistRole from PicklistRole picklistRole left join fetch picklistRole.roleType")
    List<PicklistRole> findAllWithToOneRelationships();

    @Query("select picklistRole from PicklistRole picklistRole left join fetch picklistRole.roleType where picklistRole.id =:id")
    Optional<PicklistRole> findOneWithToOneRelationships(@Param("id") Long id);
}
