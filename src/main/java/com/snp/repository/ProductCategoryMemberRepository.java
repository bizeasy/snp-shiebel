package com.snp.repository;

import com.snp.domain.ProductCategoryMember;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductCategoryMember entity.
 */
@Repository
public interface ProductCategoryMemberRepository
    extends JpaRepository<ProductCategoryMember, Long>, JpaSpecificationExecutor<ProductCategoryMember>
{
    default Optional<ProductCategoryMember> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProductCategoryMember> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProductCategoryMember> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select productCategoryMember from ProductCategoryMember productCategoryMember left join fetch productCategoryMember.product left join fetch productCategoryMember.productCategory",
        countQuery = "select count(productCategoryMember) from ProductCategoryMember productCategoryMember"
    )
    Page<ProductCategoryMember> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select productCategoryMember from ProductCategoryMember productCategoryMember left join fetch productCategoryMember.product left join fetch productCategoryMember.productCategory"
    )
    List<ProductCategoryMember> findAllWithToOneRelationships();

    @Query(
        "select productCategoryMember from ProductCategoryMember productCategoryMember left join fetch productCategoryMember.product left join fetch productCategoryMember.productCategory where productCategoryMember.id =:id"
    )
    Optional<ProductCategoryMember> findOneWithToOneRelationships(@Param("id") Long id);
}
