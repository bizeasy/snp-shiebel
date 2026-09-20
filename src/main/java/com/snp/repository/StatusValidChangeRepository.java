package com.snp.repository;

import com.snp.domain.StatusValidChange;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StatusValidChange entity.
 */
@Repository
public interface StatusValidChangeRepository extends JpaRepository<StatusValidChange, Long>, JpaSpecificationExecutor<StatusValidChange> {
    default Optional<StatusValidChange> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<StatusValidChange> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<StatusValidChange> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select statusValidChange from StatusValidChange statusValidChange left join fetch statusValidChange.status left join fetch statusValidChange.statusTo",
        countQuery = "select count(statusValidChange) from StatusValidChange statusValidChange"
    )
    Page<StatusValidChange> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select statusValidChange from StatusValidChange statusValidChange left join fetch statusValidChange.status left join fetch statusValidChange.statusTo"
    )
    List<StatusValidChange> findAllWithToOneRelationships();

    @Query(
        "select statusValidChange from StatusValidChange statusValidChange left join fetch statusValidChange.status left join fetch statusValidChange.statusTo where statusValidChange.id =:id"
    )
    Optional<StatusValidChange> findOneWithToOneRelationships(@Param("id") Long id);
}
