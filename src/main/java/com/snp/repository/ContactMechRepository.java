package com.snp.repository;

import com.snp.domain.ContactMech;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ContactMech entity.
 */
@Repository
public interface ContactMechRepository extends JpaRepository<ContactMech, Long>, JpaSpecificationExecutor<ContactMech> {
    default Optional<ContactMech> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ContactMech> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ContactMech> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select contactMech from ContactMech contactMech left join fetch contactMech.contactMechType",
        countQuery = "select count(contactMech) from ContactMech contactMech"
    )
    Page<ContactMech> findAllWithToOneRelationships(Pageable pageable);

    @Query("select contactMech from ContactMech contactMech left join fetch contactMech.contactMechType")
    List<ContactMech> findAllWithToOneRelationships();

    @Query("select contactMech from ContactMech contactMech left join fetch contactMech.contactMechType where contactMech.id =:id")
    Optional<ContactMech> findOneWithToOneRelationships(@Param("id") Long id);
}
