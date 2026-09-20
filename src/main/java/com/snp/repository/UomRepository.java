package com.snp.repository;

import com.snp.domain.Uom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Uom entity.
 */
@Repository
public interface UomRepository extends JpaRepository<Uom, Long>, JpaSpecificationExecutor<Uom> {
    default Optional<Uom> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Uom> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Uom> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select uom from Uom uom left join fetch uom.uomType", countQuery = "select count(uom) from Uom uom")
    Page<Uom> findAllWithToOneRelationships(Pageable pageable);

    @Query("select uom from Uom uom left join fetch uom.uomType")
    List<Uom> findAllWithToOneRelationships();

    @Query("select uom from Uom uom left join fetch uom.uomType where uom.id =:id")
    Optional<Uom> findOneWithToOneRelationships(@Param("id") Long id);
}
