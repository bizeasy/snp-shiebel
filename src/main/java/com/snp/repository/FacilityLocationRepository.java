package com.snp.repository;

import com.snp.domain.FacilityLocation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FacilityLocation entity.
 */
@Repository
public interface FacilityLocationRepository extends JpaRepository<FacilityLocation, Long>, JpaSpecificationExecutor<FacilityLocation> {
    default Optional<FacilityLocation> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<FacilityLocation> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<FacilityLocation> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select facilityLocation from FacilityLocation facilityLocation left join fetch facilityLocation.facility left join fetch facilityLocation.locationType",
        countQuery = "select count(facilityLocation) from FacilityLocation facilityLocation"
    )
    Page<FacilityLocation> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select facilityLocation from FacilityLocation facilityLocation left join fetch facilityLocation.facility left join fetch facilityLocation.locationType"
    )
    List<FacilityLocation> findAllWithToOneRelationships();

    @Query(
        "select facilityLocation from FacilityLocation facilityLocation left join fetch facilityLocation.facility left join fetch facilityLocation.locationType where facilityLocation.id =:id"
    )
    Optional<FacilityLocation> findOneWithToOneRelationships(@Param("id") Long id);
}
