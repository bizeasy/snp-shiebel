package com.snp.repository;

import com.snp.domain.CustRequestType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CustRequestType entity.
 */
@Repository
public interface CustRequestTypeRepository extends JpaRepository<CustRequestType, Long>, JpaSpecificationExecutor<CustRequestType> {
    default Optional<CustRequestType> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CustRequestType> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CustRequestType> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select custRequestType from CustRequestType custRequestType left join fetch custRequestType.parent",
        countQuery = "select count(custRequestType) from CustRequestType custRequestType"
    )
    Page<CustRequestType> findAllWithToOneRelationships(Pageable pageable);

    @Query("select custRequestType from CustRequestType custRequestType left join fetch custRequestType.parent")
    List<CustRequestType> findAllWithToOneRelationships();

    @Query(
        "select custRequestType from CustRequestType custRequestType left join fetch custRequestType.parent where custRequestType.id =:id"
    )
    Optional<CustRequestType> findOneWithToOneRelationships(@Param("id") Long id);
}
