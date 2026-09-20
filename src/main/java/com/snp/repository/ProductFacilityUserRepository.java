package com.snp.repository;

import com.snp.domain.ProductFacilityUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductFacilityUser entity.
 */
@Repository
public interface ProductFacilityUserRepository
    extends JpaRepository<ProductFacilityUser, Long>, JpaSpecificationExecutor<ProductFacilityUser>
{
    default Optional<ProductFacilityUser> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProductFacilityUser> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProductFacilityUser> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select productFacilityUser from ProductFacilityUser productFacilityUser left join fetch productFacilityUser.status",
        countQuery = "select count(productFacilityUser) from ProductFacilityUser productFacilityUser"
    )
    Page<ProductFacilityUser> findAllWithToOneRelationships(Pageable pageable);

    @Query("select productFacilityUser from ProductFacilityUser productFacilityUser left join fetch productFacilityUser.status")
    List<ProductFacilityUser> findAllWithToOneRelationships();

    @Query(
        "select productFacilityUser from ProductFacilityUser productFacilityUser left join fetch productFacilityUser.status where productFacilityUser.id =:id"
    )
    Optional<ProductFacilityUser> findOneWithToOneRelationships(@Param("id") Long id);
}
