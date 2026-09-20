package com.snp.repository;

import com.snp.domain.ProductKeyword;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductKeyword entity.
 */
@Repository
public interface ProductKeywordRepository extends JpaRepository<ProductKeyword, Long>, JpaSpecificationExecutor<ProductKeyword> {
    default Optional<ProductKeyword> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProductKeyword> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProductKeyword> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select productKeyword from ProductKeyword productKeyword left join fetch productKeyword.product left join fetch productKeyword.keywordType",
        countQuery = "select count(productKeyword) from ProductKeyword productKeyword"
    )
    Page<ProductKeyword> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select productKeyword from ProductKeyword productKeyword left join fetch productKeyword.product left join fetch productKeyword.keywordType"
    )
    List<ProductKeyword> findAllWithToOneRelationships();

    @Query(
        "select productKeyword from ProductKeyword productKeyword left join fetch productKeyword.product left join fetch productKeyword.keywordType where productKeyword.id =:id"
    )
    Optional<ProductKeyword> findOneWithToOneRelationships(@Param("id") Long id);
}
