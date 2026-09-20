package com.snp.repository;

import com.snp.domain.ProductAssoc;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductAssoc entity.
 */
@Repository
public interface ProductAssocRepository extends JpaRepository<ProductAssoc, Long>, JpaSpecificationExecutor<ProductAssoc> {
    default Optional<ProductAssoc> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProductAssoc> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProductAssoc> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select productAssoc from ProductAssoc productAssoc left join fetch productAssoc.product left join fetch productAssoc.productTo left join fetch productAssoc.productAssocType",
        countQuery = "select count(productAssoc) from ProductAssoc productAssoc"
    )
    Page<ProductAssoc> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select productAssoc from ProductAssoc productAssoc left join fetch productAssoc.product left join fetch productAssoc.productTo left join fetch productAssoc.productAssocType"
    )
    List<ProductAssoc> findAllWithToOneRelationships();

    @Query(
        "select productAssoc from ProductAssoc productAssoc left join fetch productAssoc.product left join fetch productAssoc.productTo left join fetch productAssoc.productAssocType where productAssoc.id =:id"
    )
    Optional<ProductAssoc> findOneWithToOneRelationships(@Param("id") Long id);
}
