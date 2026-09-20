package com.snp.repository;

import com.snp.domain.CustRequestItemAttachment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CustRequestItemAttachment entity.
 */
@Repository
public interface CustRequestItemAttachmentRepository
    extends JpaRepository<CustRequestItemAttachment, Long>, JpaSpecificationExecutor<CustRequestItemAttachment>
{
    default Optional<CustRequestItemAttachment> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CustRequestItemAttachment> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CustRequestItemAttachment> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select custRequestItemAttachment from CustRequestItemAttachment custRequestItemAttachment left join fetch custRequestItemAttachment.attachment",
        countQuery = "select count(custRequestItemAttachment) from CustRequestItemAttachment custRequestItemAttachment"
    )
    Page<CustRequestItemAttachment> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select custRequestItemAttachment from CustRequestItemAttachment custRequestItemAttachment left join fetch custRequestItemAttachment.attachment"
    )
    List<CustRequestItemAttachment> findAllWithToOneRelationships();

    @Query(
        "select custRequestItemAttachment from CustRequestItemAttachment custRequestItemAttachment left join fetch custRequestItemAttachment.attachment where custRequestItemAttachment.id =:id"
    )
    Optional<CustRequestItemAttachment> findOneWithToOneRelationships(@Param("id") Long id);
}
