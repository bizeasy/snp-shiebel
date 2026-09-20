package com.snp.repository;

import com.snp.domain.FacilityGroup;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FacilityGroup entity.
 */
@Repository
public interface FacilityGroupRepository extends JpaRepository<FacilityGroup, Long>, JpaSpecificationExecutor<FacilityGroup> {
    default Optional<FacilityGroup> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<FacilityGroup> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<FacilityGroup> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select facilityGroup from FacilityGroup facilityGroup left join fetch facilityGroup.facilityGroupType left join fetch facilityGroup.parentFacilityGroup",
        countQuery = "select count(facilityGroup) from FacilityGroup facilityGroup"
    )
    Page<FacilityGroup> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select facilityGroup from FacilityGroup facilityGroup left join fetch facilityGroup.facilityGroupType left join fetch facilityGroup.parentFacilityGroup"
    )
    List<FacilityGroup> findAllWithToOneRelationships();

    @Query(
        "select facilityGroup from FacilityGroup facilityGroup left join fetch facilityGroup.facilityGroupType left join fetch facilityGroup.parentFacilityGroup where facilityGroup.id =:id"
    )
    Optional<FacilityGroup> findOneWithToOneRelationships(@Param("id") Long id);
}
