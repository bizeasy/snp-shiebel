package com.snp.repository;

import com.snp.domain.FacilityContactMech;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FacilityContactMech entity.
 */
@Repository
public interface FacilityContactMechRepository
    extends JpaRepository<FacilityContactMech, Long>, JpaSpecificationExecutor<FacilityContactMech>
{
    default Optional<FacilityContactMech> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<FacilityContactMech> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<FacilityContactMech> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select facilityContactMech from FacilityContactMech facilityContactMech left join fetch facilityContactMech.facility left join fetch facilityContactMech.contactMechPurpose",
        countQuery = "select count(facilityContactMech) from FacilityContactMech facilityContactMech"
    )
    Page<FacilityContactMech> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select facilityContactMech from FacilityContactMech facilityContactMech left join fetch facilityContactMech.facility left join fetch facilityContactMech.contactMechPurpose"
    )
    List<FacilityContactMech> findAllWithToOneRelationships();

    @Query(
        "select facilityContactMech from FacilityContactMech facilityContactMech left join fetch facilityContactMech.facility left join fetch facilityContactMech.contactMechPurpose where facilityContactMech.id =:id"
    )
    Optional<FacilityContactMech> findOneWithToOneRelationships(@Param("id") Long id);
}
