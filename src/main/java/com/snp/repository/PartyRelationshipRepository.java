package com.snp.repository;

import com.snp.domain.PartyRelationship;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PartyRelationship entity.
 */
@Repository
public interface PartyRelationshipRepository extends JpaRepository<PartyRelationship, Long>, JpaSpecificationExecutor<PartyRelationship> {
    default Optional<PartyRelationship> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PartyRelationship> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PartyRelationship> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select partyRelationship from PartyRelationship partyRelationship left join fetch partyRelationship.roleTypeFrom left join fetch partyRelationship.roleTypeTo left join fetch partyRelationship.relationshipType",
        countQuery = "select count(partyRelationship) from PartyRelationship partyRelationship"
    )
    Page<PartyRelationship> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select partyRelationship from PartyRelationship partyRelationship left join fetch partyRelationship.roleTypeFrom left join fetch partyRelationship.roleTypeTo left join fetch partyRelationship.relationshipType"
    )
    List<PartyRelationship> findAllWithToOneRelationships();

    @Query(
        "select partyRelationship from PartyRelationship partyRelationship left join fetch partyRelationship.roleTypeFrom left join fetch partyRelationship.roleTypeTo left join fetch partyRelationship.relationshipType where partyRelationship.id =:id"
    )
    Optional<PartyRelationship> findOneWithToOneRelationships(@Param("id") Long id);
}
