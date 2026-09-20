package com.snp.repository;

import com.snp.domain.PartyRelationshipType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PartyRelationshipType entity.
 */
@Repository
public interface PartyRelationshipTypeRepository
    extends JpaRepository<PartyRelationshipType, Long>, JpaSpecificationExecutor<PartyRelationshipType>
{
    default Optional<PartyRelationshipType> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PartyRelationshipType> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PartyRelationshipType> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select partyRelationshipType from PartyRelationshipType partyRelationshipType left join fetch partyRelationshipType.roleTypeFrom left join fetch partyRelationshipType.roleTypeTo",
        countQuery = "select count(partyRelationshipType) from PartyRelationshipType partyRelationshipType"
    )
    Page<PartyRelationshipType> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select partyRelationshipType from PartyRelationshipType partyRelationshipType left join fetch partyRelationshipType.roleTypeFrom left join fetch partyRelationshipType.roleTypeTo"
    )
    List<PartyRelationshipType> findAllWithToOneRelationships();

    @Query(
        "select partyRelationshipType from PartyRelationshipType partyRelationshipType left join fetch partyRelationshipType.roleTypeFrom left join fetch partyRelationshipType.roleTypeTo where partyRelationshipType.id =:id"
    )
    Optional<PartyRelationshipType> findOneWithToOneRelationships(@Param("id") Long id);
}
