package com.snp.repository;

import com.snp.domain.Geo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Geo entity.
 */
@Repository
public interface GeoRepository extends JpaRepository<Geo, Long>, JpaSpecificationExecutor<Geo> {
    default Optional<Geo> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Geo> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Geo> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select geo from Geo geo left join fetch geo.geoType", countQuery = "select count(geo) from Geo geo")
    Page<Geo> findAllWithToOneRelationships(Pageable pageable);

    @Query("select geo from Geo geo left join fetch geo.geoType")
    List<Geo> findAllWithToOneRelationships();

    @Query("select geo from Geo geo left join fetch geo.geoType where geo.id =:id")
    Optional<Geo> findOneWithToOneRelationships(@Param("id") Long id);
}
