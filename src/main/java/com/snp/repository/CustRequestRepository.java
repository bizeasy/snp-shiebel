package com.snp.repository;

import com.snp.domain.CustRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CustRequest entity.
 */
@Repository
public interface CustRequestRepository extends JpaRepository<CustRequest, Long>, JpaSpecificationExecutor<CustRequest> {
    default Optional<CustRequest> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CustRequest> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CustRequest> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select custRequest from CustRequest custRequest left join fetch custRequest.custRequestType left join fetch custRequest.custRequestCategory left join fetch custRequest.status left join fetch custRequest.productStore left join fetch custRequest.salesChannel left join fetch custRequest.currencyUom",
        countQuery = "select count(custRequest) from CustRequest custRequest"
    )
    Page<CustRequest> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select custRequest from CustRequest custRequest left join fetch custRequest.custRequestType left join fetch custRequest.custRequestCategory left join fetch custRequest.status left join fetch custRequest.productStore left join fetch custRequest.salesChannel left join fetch custRequest.currencyUom"
    )
    List<CustRequest> findAllWithToOneRelationships();

    @Query(
        "select custRequest from CustRequest custRequest left join fetch custRequest.custRequestType left join fetch custRequest.custRequestCategory left join fetch custRequest.status left join fetch custRequest.productStore left join fetch custRequest.salesChannel left join fetch custRequest.currencyUom where custRequest.id =:id"
    )
    Optional<CustRequest> findOneWithToOneRelationships(@Param("id") Long id);
}
