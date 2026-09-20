package com.snp.repository;

import com.snp.domain.OrderItemAttachment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrderItemAttachment entity.
 */
@Repository
public interface OrderItemAttachmentRepository
    extends JpaRepository<OrderItemAttachment, Long>, JpaSpecificationExecutor<OrderItemAttachment>
{
    default Optional<OrderItemAttachment> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<OrderItemAttachment> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<OrderItemAttachment> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select orderItemAttachment from OrderItemAttachment orderItemAttachment left join fetch orderItemAttachment.attachment",
        countQuery = "select count(orderItemAttachment) from OrderItemAttachment orderItemAttachment"
    )
    Page<OrderItemAttachment> findAllWithToOneRelationships(Pageable pageable);

    @Query("select orderItemAttachment from OrderItemAttachment orderItemAttachment left join fetch orderItemAttachment.attachment")
    List<OrderItemAttachment> findAllWithToOneRelationships();

    @Query(
        "select orderItemAttachment from OrderItemAttachment orderItemAttachment left join fetch orderItemAttachment.attachment where orderItemAttachment.id =:id"
    )
    Optional<OrderItemAttachment> findOneWithToOneRelationships(@Param("id") Long id);
}
