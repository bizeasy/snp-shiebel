package com.snp.repository;

import com.snp.domain.FacilityParty;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FacilityParty entity.
 */
@Repository
public interface FacilityPartyRepository extends JpaRepository<FacilityParty, Long>, JpaSpecificationExecutor<FacilityParty> {
    default Optional<FacilityParty> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<FacilityParty> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<FacilityParty> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select facilityParty from FacilityParty facilityParty left join fetch facilityParty.facility left join fetch facilityParty.roleType",
        countQuery = "select count(facilityParty) from FacilityParty facilityParty"
    )
    Page<FacilityParty> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select facilityParty from FacilityParty facilityParty left join fetch facilityParty.facility left join fetch facilityParty.roleType"
    )
    List<FacilityParty> findAllWithToOneRelationships();

    @Query(
        "select facilityParty from FacilityParty facilityParty left join fetch facilityParty.facility left join fetch facilityParty.roleType where facilityParty.id =:id"
    )
    Optional<FacilityParty> findOneWithToOneRelationships(@Param("id") Long id);
}
