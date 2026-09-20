package com.snp.repository;

import com.snp.domain.CatalogueCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CatalogueCategory entity.
 */
@Repository
public interface CatalogueCategoryRepository extends JpaRepository<CatalogueCategory, Long>, JpaSpecificationExecutor<CatalogueCategory> {
    default Optional<CatalogueCategory> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CatalogueCategory> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CatalogueCategory> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select catalogueCategory from CatalogueCategory catalogueCategory left join fetch catalogueCategory.catalogue left join fetch catalogueCategory.productCategory left join fetch catalogueCategory.catalogueCategoryType",
        countQuery = "select count(catalogueCategory) from CatalogueCategory catalogueCategory"
    )
    Page<CatalogueCategory> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select catalogueCategory from CatalogueCategory catalogueCategory left join fetch catalogueCategory.catalogue left join fetch catalogueCategory.productCategory left join fetch catalogueCategory.catalogueCategoryType"
    )
    List<CatalogueCategory> findAllWithToOneRelationships();

    @Query(
        "select catalogueCategory from CatalogueCategory catalogueCategory left join fetch catalogueCategory.catalogue left join fetch catalogueCategory.productCategory left join fetch catalogueCategory.catalogueCategoryType where catalogueCategory.id =:id"
    )
    Optional<CatalogueCategory> findOneWithToOneRelationships(@Param("id") Long id);
}
