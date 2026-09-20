package com.snp.repository;

import com.snp.domain.TaxAuthority;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TaxAuthority entity.
 */
@Repository
public interface TaxAuthorityRepository extends JpaRepository<TaxAuthority, Long>, JpaSpecificationExecutor<TaxAuthority> {
    default Optional<TaxAuthority> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<TaxAuthority> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<TaxAuthority> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select taxAuthority from TaxAuthority taxAuthority left join fetch taxAuthority.geo",
        countQuery = "select count(taxAuthority) from TaxAuthority taxAuthority"
    )
    Page<TaxAuthority> findAllWithToOneRelationships(Pageable pageable);

    @Query("select taxAuthority from TaxAuthority taxAuthority left join fetch taxAuthority.geo")
    List<TaxAuthority> findAllWithToOneRelationships();

    @Query("select taxAuthority from TaxAuthority taxAuthority left join fetch taxAuthority.geo where taxAuthority.id =:id")
    Optional<TaxAuthority> findOneWithToOneRelationships(@Param("id") Long id);
}
