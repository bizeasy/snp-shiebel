package com.snp.repository;

import com.snp.domain.PartyIdentification;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PartyIdentification entity.
 */
@Repository
public interface PartyIdentificationRepository
    extends JpaRepository<PartyIdentification, Long>, JpaSpecificationExecutor<PartyIdentification>
{
    default Optional<PartyIdentification> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PartyIdentification> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PartyIdentification> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select partyIdentification from PartyIdentification partyIdentification left join fetch partyIdentification.type",
        countQuery = "select count(partyIdentification) from PartyIdentification partyIdentification"
    )
    Page<PartyIdentification> findAllWithToOneRelationships(Pageable pageable);

    @Query("select partyIdentification from PartyIdentification partyIdentification left join fetch partyIdentification.type")
    List<PartyIdentification> findAllWithToOneRelationships();

    @Query(
        "select partyIdentification from PartyIdentification partyIdentification left join fetch partyIdentification.type where partyIdentification.id =:id"
    )
    Optional<PartyIdentification> findOneWithToOneRelationships(@Param("id") Long id);
}
