package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.OrderItemAttachment;
import com.snp.repository.OrderItemAttachmentRepository;
import com.snp.service.criteria.OrderItemAttachmentCriteria;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link OrderItemAttachment} entities in the database.
 * The main input is a {@link OrderItemAttachmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OrderItemAttachment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderItemAttachmentQueryService extends QueryService<OrderItemAttachment> {

    private static final Logger LOG = LoggerFactory.getLogger(OrderItemAttachmentQueryService.class);

    private final OrderItemAttachmentRepository orderItemAttachmentRepository;

    public OrderItemAttachmentQueryService(OrderItemAttachmentRepository orderItemAttachmentRepository) {
        this.orderItemAttachmentRepository = orderItemAttachmentRepository;
    }

    /**
     * Return a {@link Page} of {@link OrderItemAttachment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderItemAttachment> findByCriteria(OrderItemAttachmentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderItemAttachment> specification = createSpecification(criteria);
        return orderItemAttachmentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderItemAttachmentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OrderItemAttachment> specification = createSpecification(criteria);
        return orderItemAttachmentRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderItemAttachmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderItemAttachment> createSpecification(OrderItemAttachmentCriteria criteria) {
        Specification<OrderItemAttachment> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(OrderItemAttachment_.orderItem, JoinType.LEFT);
                root.fetch(OrderItemAttachment_.attachment, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), OrderItemAttachment_.id),
                    buildStringSpecification(criteria.getName(), OrderItemAttachment_.name),
                    buildSpecification(criteria.getOrderItemId(), root ->
                        root.join(OrderItemAttachment_.orderItem, JoinType.LEFT).get(OrderItem_.id)
                    ),
                    buildSpecification(criteria.getAttachmentId(), root ->
                        root.join(OrderItemAttachment_.attachment, JoinType.LEFT).get(Attachment_.id)
                    )
                )
            );
        }
        return specification;
    }
}
