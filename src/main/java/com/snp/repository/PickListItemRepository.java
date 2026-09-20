package com.snp.repository;

import com.snp.domain.PickListItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PickListItem entity.
 */
@Repository
public interface PickListItemRepository extends JpaRepository<PickListItem, Long>, JpaSpecificationExecutor<PickListItem> {
    default Optional<PickListItem> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PickListItem> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PickListItem> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select pickListItem from PickListItem pickListItem left join fetch pickListItem.product left join fetch pickListItem.facilityLocation left join fetch pickListItem.status",
        countQuery = "select count(pickListItem) from PickListItem pickListItem"
    )
    Page<PickListItem> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select pickListItem from PickListItem pickListItem left join fetch pickListItem.product left join fetch pickListItem.facilityLocation left join fetch pickListItem.status"
    )
    List<PickListItem> findAllWithToOneRelationships();

    @Query(
        "select pickListItem from PickListItem pickListItem left join fetch pickListItem.product left join fetch pickListItem.facilityLocation left join fetch pickListItem.status where pickListItem.id =:id"
    )
    Optional<PickListItem> findOneWithToOneRelationships(@Param("id") Long id);
}
