package com.snp.repository;

import com.snp.domain.PickList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PickList entity.
 */
@Repository
public interface PickListRepository extends JpaRepository<PickList, Long>, JpaSpecificationExecutor<PickList> {
    default Optional<PickList> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PickList> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PickList> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select pickList from PickList pickList left join fetch pickList.facility left join fetch pickList.status",
        countQuery = "select count(pickList) from PickList pickList"
    )
    Page<PickList> findAllWithToOneRelationships(Pageable pageable);

    @Query("select pickList from PickList pickList left join fetch pickList.facility left join fetch pickList.status")
    List<PickList> findAllWithToOneRelationships();

    @Query(
        "select pickList from PickList pickList left join fetch pickList.facility left join fetch pickList.status where pickList.id =:id"
    )
    Optional<PickList> findOneWithToOneRelationships(@Param("id") Long id);
}
