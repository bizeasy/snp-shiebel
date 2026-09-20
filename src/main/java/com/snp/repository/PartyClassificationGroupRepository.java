package com.snp.repository;

import com.snp.domain.PartyClassificationGroup;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PartyClassificationGroup entity.
 */
@Repository
public interface PartyClassificationGroupRepository
    extends JpaRepository<PartyClassificationGroup, Long>, JpaSpecificationExecutor<PartyClassificationGroup>
{
    default Optional<PartyClassificationGroup> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PartyClassificationGroup> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PartyClassificationGroup> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select partyClassificationGroup from PartyClassificationGroup partyClassificationGroup left join fetch partyClassificationGroup.classificationType",
        countQuery = "select count(partyClassificationGroup) from PartyClassificationGroup partyClassificationGroup"
    )
    Page<PartyClassificationGroup> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select partyClassificationGroup from PartyClassificationGroup partyClassificationGroup left join fetch partyClassificationGroup.classificationType"
    )
    List<PartyClassificationGroup> findAllWithToOneRelationships();

    @Query(
        "select partyClassificationGroup from PartyClassificationGroup partyClassificationGroup left join fetch partyClassificationGroup.classificationType where partyClassificationGroup.id =:id"
    )
    Optional<PartyClassificationGroup> findOneWithToOneRelationships(@Param("id") Long id);
}
