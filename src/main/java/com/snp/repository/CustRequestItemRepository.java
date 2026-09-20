package com.snp.repository;

import com.snp.domain.CustRequestItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CustRequestItem entity.
 */
@Repository
public interface CustRequestItemRepository extends JpaRepository<CustRequestItem, Long>, JpaSpecificationExecutor<CustRequestItem> {
    default Optional<CustRequestItem> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CustRequestItem> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CustRequestItem> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select custRequestItem from CustRequestItem custRequestItem left join fetch custRequestItem.status left join fetch custRequestItem.product left join fetch custRequestItem.uom",
        countQuery = "select count(custRequestItem) from CustRequestItem custRequestItem"
    )
    Page<CustRequestItem> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select custRequestItem from CustRequestItem custRequestItem left join fetch custRequestItem.status left join fetch custRequestItem.product left join fetch custRequestItem.uom"
    )
    List<CustRequestItem> findAllWithToOneRelationships();

    @Query(
        "select custRequestItem from CustRequestItem custRequestItem left join fetch custRequestItem.status left join fetch custRequestItem.product left join fetch custRequestItem.uom where custRequestItem.id =:id"
    )
    Optional<CustRequestItem> findOneWithToOneRelationships(@Param("id") Long id);
}
