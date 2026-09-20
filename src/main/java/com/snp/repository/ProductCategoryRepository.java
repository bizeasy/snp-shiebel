package com.snp.repository;

import com.snp.domain.ProductCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductCategory entity.
 */
@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long>, JpaSpecificationExecutor<ProductCategory> {
    default Optional<ProductCategory> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProductCategory> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProductCategory> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select productCategory from ProductCategory productCategory left join fetch productCategory.productCategoryType left join fetch productCategory.parent",
        countQuery = "select count(productCategory) from ProductCategory productCategory"
    )
    Page<ProductCategory> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select productCategory from ProductCategory productCategory left join fetch productCategory.productCategoryType left join fetch productCategory.parent"
    )
    List<ProductCategory> findAllWithToOneRelationships();

    @Query(
        "select productCategory from ProductCategory productCategory left join fetch productCategory.productCategoryType left join fetch productCategory.parent where productCategory.id =:id"
    )
    Optional<ProductCategory> findOneWithToOneRelationships(@Param("id") Long id);
}
