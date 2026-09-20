package com.snp.repository;

import com.snp.domain.GeoAssoc;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GeoAssoc entity.
 */
@Repository
public interface GeoAssocRepository extends JpaRepository<GeoAssoc, Long>, JpaSpecificationExecutor<GeoAssoc> {
    default Optional<GeoAssoc> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<GeoAssoc> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<GeoAssoc> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select geoAssoc from GeoAssoc geoAssoc left join fetch geoAssoc.geo left join fetch geoAssoc.geoTo left join fetch geoAssoc.geoAssocType",
        countQuery = "select count(geoAssoc) from GeoAssoc geoAssoc"
    )
    Page<GeoAssoc> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select geoAssoc from GeoAssoc geoAssoc left join fetch geoAssoc.geo left join fetch geoAssoc.geoTo left join fetch geoAssoc.geoAssocType"
    )
    List<GeoAssoc> findAllWithToOneRelationships();

    @Query(
        "select geoAssoc from GeoAssoc geoAssoc left join fetch geoAssoc.geo left join fetch geoAssoc.geoTo left join fetch geoAssoc.geoAssocType where geoAssoc.id =:id"
    )
    Optional<GeoAssoc> findOneWithToOneRelationships(@Param("id") Long id);
}
