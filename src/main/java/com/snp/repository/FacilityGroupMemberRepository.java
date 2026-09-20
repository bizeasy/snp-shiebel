package com.snp.repository;

import com.snp.domain.FacilityGroupMember;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FacilityGroupMember entity.
 */
@Repository
public interface FacilityGroupMemberRepository
    extends JpaRepository<FacilityGroupMember, Long>, JpaSpecificationExecutor<FacilityGroupMember>
{
    default Optional<FacilityGroupMember> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<FacilityGroupMember> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<FacilityGroupMember> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select facilityGroupMember from FacilityGroupMember facilityGroupMember left join fetch facilityGroupMember.facility left join fetch facilityGroupMember.facilityGroup",
        countQuery = "select count(facilityGroupMember) from FacilityGroupMember facilityGroupMember"
    )
    Page<FacilityGroupMember> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select facilityGroupMember from FacilityGroupMember facilityGroupMember left join fetch facilityGroupMember.facility left join fetch facilityGroupMember.facilityGroup"
    )
    List<FacilityGroupMember> findAllWithToOneRelationships();

    @Query(
        "select facilityGroupMember from FacilityGroupMember facilityGroupMember left join fetch facilityGroupMember.facility left join fetch facilityGroupMember.facilityGroup where facilityGroupMember.id =:id"
    )
    Optional<FacilityGroupMember> findOneWithToOneRelationships(@Param("id") Long id);
}
