package com.snp.repository;

import com.snp.domain.ReturnItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReturnItem entity.
 */
@Repository
public interface ReturnItemRepository extends JpaRepository<ReturnItem, Long>, JpaSpecificationExecutor<ReturnItem> {
    default Optional<ReturnItem> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ReturnItem> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ReturnItem> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select returnItem from ReturnItem returnItem left join fetch returnItem.returnType left join fetch returnItem.product left join fetch returnItem.status",
        countQuery = "select count(returnItem) from ReturnItem returnItem"
    )
    Page<ReturnItem> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select returnItem from ReturnItem returnItem left join fetch returnItem.returnType left join fetch returnItem.product left join fetch returnItem.status"
    )
    List<ReturnItem> findAllWithToOneRelationships();

    @Query(
        "select returnItem from ReturnItem returnItem left join fetch returnItem.returnType left join fetch returnItem.product left join fetch returnItem.status where returnItem.id =:id"
    )
    Optional<ReturnItem> findOneWithToOneRelationships(@Param("id") Long id);
}
