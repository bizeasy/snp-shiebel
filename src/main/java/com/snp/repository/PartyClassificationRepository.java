package com.snp.repository;

import com.snp.domain.PartyClassification;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PartyClassification entity.
 */
@Repository
public interface PartyClassificationRepository
    extends JpaRepository<PartyClassification, Long>, JpaSpecificationExecutor<PartyClassification>
{
    default Optional<PartyClassification> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PartyClassification> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PartyClassification> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select partyClassification from PartyClassification partyClassification left join fetch partyClassification.classificationGroup",
        countQuery = "select count(partyClassification) from PartyClassification partyClassification"
    )
    Page<PartyClassification> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select partyClassification from PartyClassification partyClassification left join fetch partyClassification.classificationGroup"
    )
    List<PartyClassification> findAllWithToOneRelationships();

    @Query(
        "select partyClassification from PartyClassification partyClassification left join fetch partyClassification.classificationGroup where partyClassification.id =:id"
    )
    Optional<PartyClassification> findOneWithToOneRelationships(@Param("id") Long id);
}
