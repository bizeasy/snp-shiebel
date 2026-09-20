package com.snp.repository;

import com.snp.domain.PartyContactMech;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PartyContactMech entity.
 */
@Repository
public interface PartyContactMechRepository extends JpaRepository<PartyContactMech, Long>, JpaSpecificationExecutor<PartyContactMech> {
    default Optional<PartyContactMech> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PartyContactMech> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PartyContactMech> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select partyContactMech from PartyContactMech partyContactMech left join fetch partyContactMech.contactMechPurpose",
        countQuery = "select count(partyContactMech) from PartyContactMech partyContactMech"
    )
    Page<PartyContactMech> findAllWithToOneRelationships(Pageable pageable);

    @Query("select partyContactMech from PartyContactMech partyContactMech left join fetch partyContactMech.contactMechPurpose")
    List<PartyContactMech> findAllWithToOneRelationships();

    @Query(
        "select partyContactMech from PartyContactMech partyContactMech left join fetch partyContactMech.contactMechPurpose where partyContactMech.id =:id"
    )
    Optional<PartyContactMech> findOneWithToOneRelationships(@Param("id") Long id);
}
