package com.snp.repository;

import com.snp.domain.OrderAttachment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrderAttachment entity.
 */
@Repository
public interface OrderAttachmentRepository extends JpaRepository<OrderAttachment, Long>, JpaSpecificationExecutor<OrderAttachment> {
    default Optional<OrderAttachment> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<OrderAttachment> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<OrderAttachment> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select orderAttachment from OrderAttachment orderAttachment left join fetch orderAttachment.attachment",
        countQuery = "select count(orderAttachment) from OrderAttachment orderAttachment"
    )
    Page<OrderAttachment> findAllWithToOneRelationships(Pageable pageable);

    @Query("select orderAttachment from OrderAttachment orderAttachment left join fetch orderAttachment.attachment")
    List<OrderAttachment> findAllWithToOneRelationships();

    @Query(
        "select orderAttachment from OrderAttachment orderAttachment left join fetch orderAttachment.attachment where orderAttachment.id =:id"
    )
    Optional<OrderAttachment> findOneWithToOneRelationships(@Param("id") Long id);
}
