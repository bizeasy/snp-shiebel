package com.snp.repository;

import com.snp.domain.TaxAuthorityRateType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TaxAuthorityRateType entity.
 */
@Repository
public interface TaxAuthorityRateTypeRepository
    extends JpaRepository<TaxAuthorityRateType, Long>, JpaSpecificationExecutor<TaxAuthorityRateType>
{
    default Optional<TaxAuthorityRateType> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<TaxAuthorityRateType> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<TaxAuthorityRateType> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select taxAuthorityRateType from TaxAuthorityRateType taxAuthorityRateType left join fetch taxAuthorityRateType.taxAuthority left join fetch taxAuthorityRateType.taxSlab",
        countQuery = "select count(taxAuthorityRateType) from TaxAuthorityRateType taxAuthorityRateType"
    )
    Page<TaxAuthorityRateType> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select taxAuthorityRateType from TaxAuthorityRateType taxAuthorityRateType left join fetch taxAuthorityRateType.taxAuthority left join fetch taxAuthorityRateType.taxSlab"
    )
    List<TaxAuthorityRateType> findAllWithToOneRelationships();

    @Query(
        "select taxAuthorityRateType from TaxAuthorityRateType taxAuthorityRateType left join fetch taxAuthorityRateType.taxAuthority left join fetch taxAuthorityRateType.taxSlab where taxAuthorityRateType.id =:id"
    )
    Optional<TaxAuthorityRateType> findOneWithToOneRelationships(@Param("id") Long id);
}
