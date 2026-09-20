package com.snp.repository;

import com.snp.domain.QuoteRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the QuoteRole entity.
 */
@Repository
public interface QuoteRoleRepository extends JpaRepository<QuoteRole, Long>, JpaSpecificationExecutor<QuoteRole> {
    default Optional<QuoteRole> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<QuoteRole> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<QuoteRole> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select quoteRole from QuoteRole quoteRole left join fetch quoteRole.roleType",
        countQuery = "select count(quoteRole) from QuoteRole quoteRole"
    )
    Page<QuoteRole> findAllWithToOneRelationships(Pageable pageable);

    @Query("select quoteRole from QuoteRole quoteRole left join fetch quoteRole.roleType")
    List<QuoteRole> findAllWithToOneRelationships();

    @Query("select quoteRole from QuoteRole quoteRole left join fetch quoteRole.roleType where quoteRole.id =:id")
    Optional<QuoteRole> findOneWithToOneRelationships(@Param("id") Long id);
}
