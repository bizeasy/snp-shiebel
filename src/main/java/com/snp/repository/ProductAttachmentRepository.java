package com.snp.repository;

import com.snp.domain.ProductAttachment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductAttachment entity.
 */
@Repository
public interface ProductAttachmentRepository extends JpaRepository<ProductAttachment, Long>, JpaSpecificationExecutor<ProductAttachment> {
    default Optional<ProductAttachment> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProductAttachment> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProductAttachment> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select productAttachment from ProductAttachment productAttachment left join fetch productAttachment.attachment",
        countQuery = "select count(productAttachment) from ProductAttachment productAttachment"
    )
    Page<ProductAttachment> findAllWithToOneRelationships(Pageable pageable);

    @Query("select productAttachment from ProductAttachment productAttachment left join fetch productAttachment.attachment")
    List<ProductAttachment> findAllWithToOneRelationships();

    @Query(
        "select productAttachment from ProductAttachment productAttachment left join fetch productAttachment.attachment where productAttachment.id =:id"
    )
    Optional<ProductAttachment> findOneWithToOneRelationships(@Param("id") Long id);
}
