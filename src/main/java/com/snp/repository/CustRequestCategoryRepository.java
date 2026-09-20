package com.snp.repository;

import com.snp.domain.CustRequestCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CustRequestCategory entity.
 */
@Repository
public interface CustRequestCategoryRepository
    extends JpaRepository<CustRequestCategory, Long>, JpaSpecificationExecutor<CustRequestCategory>
{
    default Optional<CustRequestCategory> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CustRequestCategory> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CustRequestCategory> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select custRequestCategory from CustRequestCategory custRequestCategory left join fetch custRequestCategory.custRequestType",
        countQuery = "select count(custRequestCategory) from CustRequestCategory custRequestCategory"
    )
    Page<CustRequestCategory> findAllWithToOneRelationships(Pageable pageable);

    @Query("select custRequestCategory from CustRequestCategory custRequestCategory left join fetch custRequestCategory.custRequestType")
    List<CustRequestCategory> findAllWithToOneRelationships();

    @Query(
        "select custRequestCategory from CustRequestCategory custRequestCategory left join fetch custRequestCategory.custRequestType where custRequestCategory.id =:id"
    )
    Optional<CustRequestCategory> findOneWithToOneRelationships(@Param("id") Long id);
}
