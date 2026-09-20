package com.snp.repository;

import com.snp.domain.CashBookAttachment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CashBookAttachment entity.
 */
@Repository
public interface CashBookAttachmentRepository
    extends JpaRepository<CashBookAttachment, Long>, JpaSpecificationExecutor<CashBookAttachment>
{
    default Optional<CashBookAttachment> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CashBookAttachment> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CashBookAttachment> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select cashBookAttachment from CashBookAttachment cashBookAttachment left join fetch cashBookAttachment.attachment",
        countQuery = "select count(cashBookAttachment) from CashBookAttachment cashBookAttachment"
    )
    Page<CashBookAttachment> findAllWithToOneRelationships(Pageable pageable);

    @Query("select cashBookAttachment from CashBookAttachment cashBookAttachment left join fetch cashBookAttachment.attachment")
    List<CashBookAttachment> findAllWithToOneRelationships();

    @Query(
        "select cashBookAttachment from CashBookAttachment cashBookAttachment left join fetch cashBookAttachment.attachment where cashBookAttachment.id =:id"
    )
    Optional<CashBookAttachment> findOneWithToOneRelationships(@Param("id") Long id);
}
