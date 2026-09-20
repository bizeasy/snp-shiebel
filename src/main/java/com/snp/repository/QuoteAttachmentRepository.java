package com.snp.repository;

import com.snp.domain.QuoteAttachment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the QuoteAttachment entity.
 */
@Repository
public interface QuoteAttachmentRepository extends JpaRepository<QuoteAttachment, Long>, JpaSpecificationExecutor<QuoteAttachment> {
    default Optional<QuoteAttachment> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<QuoteAttachment> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<QuoteAttachment> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select quoteAttachment from QuoteAttachment quoteAttachment left join fetch quoteAttachment.attachment",
        countQuery = "select count(quoteAttachment) from QuoteAttachment quoteAttachment"
    )
    Page<QuoteAttachment> findAllWithToOneRelationships(Pageable pageable);

    @Query("select quoteAttachment from QuoteAttachment quoteAttachment left join fetch quoteAttachment.attachment")
    List<QuoteAttachment> findAllWithToOneRelationships();

    @Query(
        "select quoteAttachment from QuoteAttachment quoteAttachment left join fetch quoteAttachment.attachment where quoteAttachment.id =:id"
    )
    Optional<QuoteAttachment> findOneWithToOneRelationships(@Param("id") Long id);
}
