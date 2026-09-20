package com.snp.repository;

import com.snp.domain.PostalAddress;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PostalAddress entity.
 */
@Repository
public interface PostalAddressRepository extends JpaRepository<PostalAddress, Long>, JpaSpecificationExecutor<PostalAddress> {
    default Optional<PostalAddress> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PostalAddress> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PostalAddress> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select postalAddress from PostalAddress postalAddress left join fetch postalAddress.state left join fetch postalAddress.pincode left join fetch postalAddress.country",
        countQuery = "select count(postalAddress) from PostalAddress postalAddress"
    )
    Page<PostalAddress> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select postalAddress from PostalAddress postalAddress left join fetch postalAddress.state left join fetch postalAddress.pincode left join fetch postalAddress.country"
    )
    List<PostalAddress> findAllWithToOneRelationships();

    @Query(
        "select postalAddress from PostalAddress postalAddress left join fetch postalAddress.state left join fetch postalAddress.pincode left join fetch postalAddress.country where postalAddress.id =:id"
    )
    Optional<PostalAddress> findOneWithToOneRelationships(@Param("id") Long id);
}
