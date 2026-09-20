package com.snp.repository;

import com.snp.domain.DepartmentMember;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DepartmentMember entity.
 */
@Repository
public interface DepartmentMemberRepository extends JpaRepository<DepartmentMember, Long>, JpaSpecificationExecutor<DepartmentMember> {
    default Optional<DepartmentMember> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DepartmentMember> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DepartmentMember> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select departmentMember from DepartmentMember departmentMember left join fetch departmentMember.department left join fetch departmentMember.roleType",
        countQuery = "select count(departmentMember) from DepartmentMember departmentMember"
    )
    Page<DepartmentMember> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select departmentMember from DepartmentMember departmentMember left join fetch departmentMember.department left join fetch departmentMember.roleType"
    )
    List<DepartmentMember> findAllWithToOneRelationships();

    @Query(
        "select departmentMember from DepartmentMember departmentMember left join fetch departmentMember.department left join fetch departmentMember.roleType where departmentMember.id =:id"
    )
    Optional<DepartmentMember> findOneWithToOneRelationships(@Param("id") Long id);
}
