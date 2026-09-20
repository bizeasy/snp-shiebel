package com.snp.repository;

import com.snp.domain.UomConversion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UomConversion entity.
 */
@Repository
public interface UomConversionRepository extends JpaRepository<UomConversion, Long>, JpaSpecificationExecutor<UomConversion> {
    default Optional<UomConversion> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<UomConversion> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<UomConversion> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select uomConversion from UomConversion uomConversion left join fetch uomConversion.fromUom left join fetch uomConversion.toUom",
        countQuery = "select count(uomConversion) from UomConversion uomConversion"
    )
    Page<UomConversion> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select uomConversion from UomConversion uomConversion left join fetch uomConversion.fromUom left join fetch uomConversion.toUom"
    )
    List<UomConversion> findAllWithToOneRelationships();

    @Query(
        "select uomConversion from UomConversion uomConversion left join fetch uomConversion.fromUom left join fetch uomConversion.toUom where uomConversion.id =:id"
    )
    Optional<UomConversion> findOneWithToOneRelationships(@Param("id") Long id);
}
