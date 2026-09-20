package com.snp.repository;

import com.snp.domain.CustRequestAttachment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CustRequestAttachment entity.
 */
@Repository
public interface CustRequestAttachmentRepository
    extends JpaRepository<CustRequestAttachment, Long>, JpaSpecificationExecutor<CustRequestAttachment>
{
    default Optional<CustRequestAttachment> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CustRequestAttachment> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CustRequestAttachment> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select custRequestAttachment from CustRequestAttachment custRequestAttachment left join fetch custRequestAttachment.attachment",
        countQuery = "select count(custRequestAttachment) from CustRequestAttachment custRequestAttachment"
    )
    Page<CustRequestAttachment> findAllWithToOneRelationships(Pageable pageable);

    @Query("select custRequestAttachment from CustRequestAttachment custRequestAttachment left join fetch custRequestAttachment.attachment")
    List<CustRequestAttachment> findAllWithToOneRelationships();

    @Query(
        "select custRequestAttachment from CustRequestAttachment custRequestAttachment left join fetch custRequestAttachment.attachment where custRequestAttachment.id =:id"
    )
    Optional<CustRequestAttachment> findOneWithToOneRelationships(@Param("id") Long id);
}
