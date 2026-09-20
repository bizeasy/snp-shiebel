package com.snp.repository;

import com.snp.domain.ReturnHeader;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReturnHeader entity.
 */
@Repository
public interface ReturnHeaderRepository extends JpaRepository<ReturnHeader, Long>, JpaSpecificationExecutor<ReturnHeader> {
    default Optional<ReturnHeader> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ReturnHeader> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ReturnHeader> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select returnHeader from ReturnHeader returnHeader left join fetch returnHeader.returnType left join fetch returnHeader.status left join fetch returnHeader.paymentMethod left join fetch returnHeader.destinationFacility left join fetch returnHeader.currencyUom",
        countQuery = "select count(returnHeader) from ReturnHeader returnHeader"
    )
    Page<ReturnHeader> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select returnHeader from ReturnHeader returnHeader left join fetch returnHeader.returnType left join fetch returnHeader.status left join fetch returnHeader.paymentMethod left join fetch returnHeader.destinationFacility left join fetch returnHeader.currencyUom"
    )
    List<ReturnHeader> findAllWithToOneRelationships();

    @Query(
        "select returnHeader from ReturnHeader returnHeader left join fetch returnHeader.returnType left join fetch returnHeader.status left join fetch returnHeader.paymentMethod left join fetch returnHeader.destinationFacility left join fetch returnHeader.currencyUom where returnHeader.id =:id"
    )
    Optional<ReturnHeader> findOneWithToOneRelationships(@Param("id") Long id);
}
