package com.snp.repository;

import com.snp.domain.PartyRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PartyRole entity.
 */
@Repository
public interface PartyRoleRepository extends JpaRepository<PartyRole, Long>, JpaSpecificationExecutor<PartyRole> {
    default Optional<PartyRole> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PartyRole> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PartyRole> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select partyRole from PartyRole partyRole left join fetch partyRole.roleType",
        countQuery = "select count(partyRole) from PartyRole partyRole"
    )
    Page<PartyRole> findAllWithToOneRelationships(Pageable pageable);

    @Query("select partyRole from PartyRole partyRole left join fetch partyRole.roleType")
    List<PartyRole> findAllWithToOneRelationships();

    @Query("select partyRole from PartyRole partyRole left join fetch partyRole.roleType where partyRole.id =:id")
    Optional<PartyRole> findOneWithToOneRelationships(@Param("id") Long id);
}
